package com.practiceit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.*;
import java.util.*;

/**************************************************
 * @author Ryan Rowe (1531352)
 *         2/12/16
 *         CSE 143 B/BP
 *         TA: Chloe Lathe
 *         Practice-It 2.0
 *         <p>
 *         Description
 **************************************************/

public abstract class Problem {
    /**
     * MySQL Connection Details
     */
    private static final String QUERY = "SELECT * FROM problems WHERE id = ? LIMIT 1";
    private static final int    TIMEOUT  = 10;

    private Map<String, Object> properties = new HashMap<>();

    public static Problem getProblem(int id)
            throws SQLException {

        Map<String, String> results = connectToDB(id);

        switch(results.get("type").toUpperCase()) {
            case "MYSTERY":
                return new Mystery(id, results);
            case "INHERITANCE":
                return new Inheritance(id, results);
            case "ARRAYINTLIST":
                return new ArrayIntListProblem(id, results);
            case "STACKQUEUE":
                return new StackQueue(id, results);
            case "METHODRETURN":
                return new MethodReturn(id, results);
            case "METHODPRINT":
                return new MethodPrint(id, results);
            case "LINKEDINTLIST":
                return new LinkedListProblem(id, results);
            default:
                throw new IllegalArgumentException("Invalid Problem Type");
        }
    }

    private static Map<String, String> connectToDB(int id) throws SQLException {
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        DriverManager.setLoginTimeout(TIMEOUT);

        try(Connection conn = DriverManager.getConnection(MySQLConnectionInfo.URL
                                                          + MySQLConnectionInfo.DB,
                                                          MySQLConnectionInfo.USERNAME,
                                                          MySQLConnectionInfo.PASSWORD);
            PreparedStatement statement = conn.prepareStatement(QUERY)) {

            statement.setInt(1, id);

            try(ResultSet resultSet = statement.executeQuery()) {
                List<Map<String, String>> list = resultSetToMap(resultSet);
                if(list.size() == 1) {
                    return list.get(0);
                } else {
                    throw new IllegalStateException("No rows found for given id: " + id);
                }
            }
        }
    }

    static List<Map<String, String>> resultSetToMap(ResultSet rs) throws SQLException {
        List<Map<String, String>> list = new ArrayList<>();
        ResultSetMetaData meta = rs.getMetaData();

        while(rs.next()) {
            Map<String, String> map = new HashMap<>();
            for(int i = 1; i <= meta.getColumnCount(); i++) {
                String key = meta.getColumnName(i);
                String value = rs.getString(key);
                map.put(key, value);
            }
            list.add(map);
        }

        return list;
    }

    /*public static Problem getProblem(JSONObject json, String attempt) {
        json = (JSONObject) json.get("problem");

        switch(((String) json.get("type")).toUpperCase()) {
            case "MYSTERY":
                return new Mystery(json, attempt);
            case "INHERITANCE":
                return new Inheritance(json, attempt);
            case "ARRAYINTLIST":
                return new ArrayIntListProblem(json, attempt);
            case "STACKQUEUE":
                return new StackQueue(json, attempt);
            case "METHODRETURN":
                return new MethodReturn(json, attempt);
            case "METHODPRINT":
                return new MethodPrint(json, attempt);
            case "LINKEDLIST":
                return new LinkedListProblem(json, attempt);
            default:
                throw new IllegalArgumentException("Invalid Problem Type");
        }
    } */

    protected Problem(int id, Map<String, String> results) {
        properties.put("id", id);
        properties.put("title", results.get("title"));
        properties.put("type", results.get("type"));
        properties.put("description", results.get("description"));
        properties.put("data", results.get("data"));
        properties.put("output", results.get("output"));
        properties.put("labels", results.get("labels"));
        properties.put("solution", results.get("solution"));
        properties.put("wrapper", results.get("wrapper"));
        properties.put("method", results.get("method"));
    }

    protected Problem(JSONObject json, String attempt) {
        properties.put("id", json.get("id"));
        properties.put("title", json.get("title"));
        properties.put("type", json.get("type"));
        properties.put("description", json.get("description"));
        properties.put("data", json.get("data"));
        properties.put("output", json.get("output"));
        properties.put("labels", json.get("labels"));
        properties.put("solution", json.get("solution"));
        properties.put("wrapper", json.get("wrapper"));
        properties.put("method", json.get("method"));
        properties.put("attempt", attempt);
    }
    abstract JSONArray run();

    abstract Map<String, Object> compile(); /*{

    }*/

    public void setAttempt(String attempt) {
        properties.put("attempt", attempt);
    }

    protected String getAttempt() {
        return (String) properties.get("attempt");
    }

    public Map<String, Object> getMap() {
        return Collections.unmodifiableMap(properties);
    }

    public JSONObject toJSON() {
        return new JSONObject(properties);
    }

    public String getTitle() {
        return (String) properties.get("title");
    }

    public String getType() {
        return (String) properties.get("type");
    }

    public String getDescription() {
        return (String) properties.get("description");
    }

    public String getLabels() {
        return (String) properties.get("labels");
    }

    public String getOutput() {
        return (String) properties.get("output");
    }

    public String getSolution() {
        return (String) properties.get("solution");
    }

    public String getWrapper() {
        return (String) properties.get("wrapper");
    }

    protected int getInsertLine() {
        String wrapper = getWrapper();
        Scanner scan = new Scanner(wrapper);
        int line = 1;
        while(scan.hasNextLine()) {
            if(scan.nextLine().contains("INSERT")) {
                return line;
            }
            line++;
        }

        return -1;
    }

    public int getId() {
        return (int) properties.get("id");
    }
}
