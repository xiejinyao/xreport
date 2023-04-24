package com.xjinyao.report.core.utils;

import com.xjinyao.report.core.definition.dataset.Field;
import com.xjinyao.report.core.exception.ReportException;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.*;
import java.util.Date;
import java.util.*;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ProcedureUtils {
	public static boolean isProcedure(String sql) {
		sql = sql.trim().toLowerCase();
		return sql.startsWith("call ");
	}

	public static List<Field> procedureColumnsQuery(String sql, Map<String, Object> pmap, Connection conn) {
		StatementWrapper wrapper = buildProcedureCallableStatement(sql, pmap, conn);
		CallableStatement cs = wrapper.getCallableStatement();
		int oracleCursorIndex = wrapper.getOracleCursorIndex();
		ResultSet rs = null;
		try {
			if (oracleCursorIndex == -1) {
				rs = cs.executeQuery();
			} else {
				cs.executeUpdate();
				rs = (ResultSet) cs.getObject(oracleCursorIndex);
			}
			ResultSetMetaData metadata = rs.getMetaData();
			int columnCount = metadata.getColumnCount();
			List<Field> fields = new ArrayList<>();
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metadata.getColumnLabel(i);
				fields.add(new Field(columnName));
			}
			return fields;
		} catch (SQLException e) {
			throw new ReportException(e);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(cs);
			JdbcUtils.closeConnection(conn);
		}
	}


	public static List<Map<String, Object>> procedureQuery(String sql, Map<String, Object> pmap, Connection conn) {
		StatementWrapper wrapper = buildProcedureCallableStatement(sql, pmap, conn);
		CallableStatement cs = wrapper.getCallableStatement();
		int oracleCursorIndex = wrapper.getOracleCursorIndex();
		ResultSet rs = null;
		try {
			if (oracleCursorIndex == -1) {
				rs = cs.executeQuery();
			} else {
				cs.executeUpdate();
				rs = (ResultSet) cs.getObject(oracleCursorIndex);
			}
			ResultSetMetaData metadata = rs.getMetaData();
			int columnCount = metadata.getColumnCount();
			List<Map<String, Object>> result = new ArrayList<>();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<>();
				for (int i = 1; i <= columnCount; i++) {
					String columnName = metadata.getColumnLabel(i);
					map.put(columnName, rs.getObject(columnName));
				}
				result.add(map);
			}
			return result;
		} catch (SQLException e) {
			throw new ReportException(e);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(cs);
			JdbcUtils.closeConnection(conn);
		}
	}

	private static StatementWrapper buildProcedureCallableStatement(String sql, Map<String, Object> pmap, Connection conn) {
		try {
			Map<String, Object> paramMap = new LinkedHashMap<>();
			int leftParnPos = sql.indexOf("(");
			int rightParnPos = sql.indexOf(")");
			String paramStr = "";
			if (leftParnPos > -1 && rightParnPos > -1) {
				paramStr = sql.substring(leftParnPos + 1, rightParnPos);
			}
			int oracleCursorIndex = -1, paramIndex = 0;
			String[] str = paramStr.split(",");
			for (String param : str) {
				paramIndex++;
				param = param.trim();
				if (param.toLowerCase().equals("oracle")) {
					sql = sql.replaceFirst(param, "?");
					oracleCursorIndex = paramIndex;
					continue;
				} else if (!param.startsWith(":")) {
					continue;
				}
				sql = sql.replaceFirst(param, "?");
				String paramName = param.substring(1, param.length());
				Object paramValue = pmap.get(paramName);
				paramMap.put(paramName, (paramValue == null ? "" : paramValue));
			}
			String procedure = "{" + sql + "}";
			CallableStatement cs = conn.prepareCall(procedure);
			int index = 1;
			for (String name : paramMap.keySet()) {
				Object value = paramMap.get(name);
				if (value instanceof String) {
					cs.setString(index, (String) value);
				} else if (value instanceof Date) {
					Date date = (Date) value;
					cs.setDate(index, new java.sql.Date(date.getTime()));
				} else if (value instanceof Integer) {
					cs.setInt(index, (Integer) value);
				} else if (value instanceof Float) {
					cs.setFloat(index, (Float) value);
				} else if (value instanceof Double) {
					cs.setDouble(index, (Double) value);
				} else {
					cs.setObject(index, value);
				}
				index++;
			}
			if (oracleCursorIndex > -1) {
				cs.registerOutParameter(oracleCursorIndex, -10);
			}
			return new StatementWrapper(cs, oracleCursorIndex);
		} catch (SQLException e) {
			throw new ReportException(e);
		}
	}
}

class StatementWrapper {
	private CallableStatement callableStatement;
	private int oracleCursorIndex;

	public StatementWrapper(CallableStatement callableStatement, int oracleCursorIndex) {
		this.callableStatement = callableStatement;
		this.oracleCursorIndex = oracleCursorIndex;
	}

	public CallableStatement getCallableStatement() {
		return callableStatement;
	}

	public int getOracleCursorIndex() {
		return oracleCursorIndex;
	}
}
