package app;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class for Managing the JDBC Connection to a SQLLite Database.
 * Allows SQL queries to be used with the SQLLite Databse in Java.
 *
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Halil Ali, 2024. email: halil.ali@rmit.edu.au
 * @editor David Eccles, 2025 email: david.eccles@rmit.edu
 */

public class JDBCConnection {

    // Name of database file (contained in database folder)
    // FIX MEEE Connection is done by using "relative path" not sure if thats recommended, we need to use database/climate.db but it doesnt work
    public static final String DATABASE = "jdbc:sqlite:cosc2803-2502-apr25-studio-project/cosc2803-2502-apr25-studio-project/database/climate02.db";  
                                            //"jdbc:sqllite:database/climate.db"

    /**
     * This creates a JDBC Object so we can keep talking to the database
     */
    public JDBCConnection() {
        System.out.println("Created JDBC Connection Object");
        
    }

    /**
     * Get all of the Flag Descriptions
     */
    // public ArrayList<FLAG> getFlags() {
    //     // Create the ArrayList of FlagQuality objects to return
    //     // Create an array called flags
    //     ArrayList<FLAG> flags = new ArrayList<FLAG>();

    //     // Setup the variable for the JDBC connection
    //     Connection connection = null;

    //     try {
    //         // Connect to JDBC database
    //         connection = DriverManager.getConnection(DATABASE);
            
    //         // Prepare a new SQL Query & Set a timeout
    //         Statement statement = connection.createStatement();
    //         // put in a timeout incase the db is not running
            
    //         statement.setQueryTimeout(30);

    //         // The SQL Query to be executed 
    //         String query = "SELECT * FROM FlagQuality";
            
    //         // Put the SQL results into a result set
    //         ResultSet results = statement.executeQuery(query);

    //         // Process all of the results
    //         while (results.next()) {
    //             // Lookup the columns we need
    //             String flagtype     = results.getString("flag");
    //             String description  = results.getString("description");

    //             // Create an FLAG Object
    //             FLAG flagsObj = new FLAG(flagtype, description);

    //             // Add the FLAG object to the flags array
    //             flags.add(flagsObj);
    //         }

    //         // Close the statement because we are done with it
    //         statement.close();
    //     } catch (SQLException e) {
    //         // If there is an error, lets just pring the error
    //         System.err.println(e.getMessage());
    //     } finally {
    //         // Safety code to cleanup
    //         try {
    //             if (connection != null) {
    //                 connection.close();
    //             }
    //         } catch (SQLException e) {
    //             // connection close failed.
    //             System.err.println(e.getMessage());
    //         }
    //     }

    //     // Finally we return all of the countries
    //     return flags;
    // }

    
    
    //below method for level 1 C data
    public ArrayList<FieldDescription> getFieldDescription() {
        // Create the ArrayList of FlagQuality objects to return
        

        // Create an array called flags
        ArrayList<FieldDescription> fieldDesc = new ArrayList<FieldDescription>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC database
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            // put in a timeout incase the db is not running
            statement.setQueryTimeout(30);

            // The SQL Query to be executed 
            String query = "Select * from Metadata;";
            
            // Put the SQL results into a result set
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                String field     = results.getString("newField");
                String description  = results.getString("newDescription");

                // Create an fielddescription Object
                FieldDescription fielddescription = new FieldDescription(field, description);
                
                // Add the fielddescritption object to the flags array
                fieldDesc.add(fielddescription);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the data
        return fieldDesc; 

    
    }
    // level 2 c methods
    public ArrayList<String> getQualityFlag() {
        ArrayList<String> QualityFlag = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT Flag From FlagQuality";
            System.out.println(query);
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                String Flag = results.getString("Flag");

                QualityFlag.add(Flag);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movies
        return QualityFlag;
    }

    public ArrayList<String> getClimateMetrics2(){
        ArrayList<String> climateMetrics2 = new ArrayList<String>();
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            //Change table name and column name
            String query = "SELECT field FROM Metadata WHERE field != 'Location' AND field != 'DMY' AND field != 'RainDaysNum' AND field != 'RainDaysMeasure' AND field != 'EvayDaysNum' AND field != 'MaxTempDays' AND field != 'MinTempDays'";
            ResultSet  results = statement.executeQuery(query);

            while (results.next()) {
                String metric2 = results.getString("field");
                climateMetrics2.add(metric2);
            }

        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return climateMetrics2;
        
    }




    public ArrayList<String> get2CMainTable(String flagQuality, String metricSelected, String startDate, String endDate, String sortOrder) {
        
    ArrayList<String> mainTable = new ArrayList<>();
    Connection connection = null;

    try {
        connection = DriverManager.getConnection(DATABASE);

        String metricColumn = metricSelected;
        String qualityColumn = metricColumn + "Qual";

        String orderClause = "ORDER BY cd.stationID, cd.DMY";
        if ("Descending".equalsIgnoreCase(sortOrder)) {
            orderClause = "ORDER BY cd.stationID DESC, cd.DMY DESC";
        }

        // Construct the query dynamically
        String query = "SELECT cd.stationID, cd.DMY, cd." + metricColumn + ", cd." + qualityColumn +
                       " FROM climatedata cd " +
                       "JOIN location l ON cd.stationID = l.site " +
                       "WHERE cd." + qualityColumn + " = ? " +
                       "AND cd.DMY BETWEEN ? AND ? " +
                       orderClause;

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setQueryTimeout(30);

        statement.setString(1, flagQuality);
        statement.setString(2, startDate);
        statement.setString(3, endDate);

        ResultSet results = statement.executeQuery();

        while (results.next()) {
            mainTable.add(results.getString("stationID"));
            mainTable.add(results.getString("DMY"));
            mainTable.add(results.getString(metricColumn));
            mainTable.add(results.getString(qualityColumn));
        }

    } catch (SQLException e) {
        // Handle exceptions appropriately
    } finally {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            // Handle close failure
        }
    }

    return mainTable;
}

   
      

public ArrayList<String> get2CSummaryData(
        String state, String monthYear, String metricQualColumn) {
    ArrayList<String> resultsList = new ArrayList<>();
    Connection connection = null;

    String query = "SELECT " +
            "cd." + metricQualColumn + " AS flag, " +
            "strftime('%m', cd.DMY) AS month, " +
            "strftime('%Y', cd.DMY) AS year, " +
            "COUNT(*) AS flag_metric_count " +
            "FROM climatedata cd " +
            "JOIN location l ON cd.stationID = l.site " +
            "WHERE l.state = ? " +
            "AND strftime('%Y-%m', cd.DMY) = ? " +
            "AND TRIM(cd." + metricQualColumn + ") != '' " +
            "AND cd." + metricQualColumn + " IS NOT NULL " +
            "AND cd." + metricQualColumn + " != 'Y' " + 
            "GROUP BY flag, month, year " +
            "ORDER BY flag_metric_count DESC";
            

    try {
        connection = DriverManager.getConnection(DATABASE);
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, state);
        statement.setString(2, monthYear);

        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            String flag = rs.getString("flag");
        
            String count = String.valueOf(rs.getInt("flag_metric_count"));

            resultsList.add(flag);
            resultsList.add(count);

        }

    } catch (SQLException e) {
        System.err.println("SQL Error in getFlagMetricCountByStateMonthYear: " + e.getMessage());
    } finally {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.err.println("Connection close failed: " + e.getMessage());
        }
    }

    return resultsList;
}

public ArrayList<String> get3CStationIds(){
        ArrayList<String> cstationid = new ArrayList<String>();
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT site FROM Location";
            ResultSet  results = statement.executeQuery(query);

            while (results.next()) {
                String csite = results.getString("site");
                
                
                cstationid.add(csite);
            }

        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return cstationid;
        
    }

    public ArrayList<String> getCCVMetrics(){
        ArrayList<String> ccvMetrics = new ArrayList<String>();
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "Select distinct metric from ClimateComparisonValue;";
            ResultSet  results = statement.executeQuery(query);

            while (results.next()) {
                String distinctMetric = results.getString("metric");
                
                
                ccvMetrics.add(distinctMetric);
            }

        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return ccvMetrics;
        
    }

    
//     public ArrayList<ArrayList<String>> get3CAnalysisData(int stationId, int startYear, int endYear, String referenceMetric) {
//     ArrayList<ArrayList<String>> results = new ArrayList<>();
//     StringBuilder selectCols = new StringBuilder("metric");
//     StringBuilder pivotExprs = new StringBuilder();

//     int startHD = (startYear / 5) * 5;
//     int endHD = ((endYear + 4) / 5) * 5;

//     // Build dynamic half-decade columns
//     for (int hd = startHD; hd <= endHD; hd += 5) {
//         selectCols.append(", \"").append(hd).append("\"");
//         pivotExprs.append(", MAX(CASE WHEN halfdecade = '").append(hd)
//                   .append("' THEN avg_value END) AS \"").append(hd).append("\"");
//     }

//     // SQL using dynamic columns and correct percent_change calculation
//     String query =
//         "WITH HalfDecadeData AS ( " +
//         "  SELECT metric, dt.halfdecade, AVG(cm.value) AS avg_value " +
//         "  FROM ClimateComparisonValue cm JOIN DateTime dt ON cm.date = dt.date " +
//         "  WHERE cm.stationID = ? AND CAST(strftime('%Y', cm.date) AS INTEGER) BETWEEN ? AND ? AND cm.value IS NOT NULL " +
//         "  GROUP BY metric, dt.halfdecade " +
//         "), Pivoted AS ( " +
//         "  SELECT metric" + pivotExprs + " FROM HalfDecadeData GROUP BY metric " +
//         "), ChangeAndTrend AS ( " +
//         "  SELECT *, " +
//         "    CASE WHEN \"" + startHD + "\" = 0 THEN 0 " +
//         "         ELSE ROUND( ( (\"" + endHD + "\" - \"" + startHD + "\") / \"" + startHD + "\") / (? - ?) * 100.0, 2 ) END AS percent_change, " +
//         "    CASE WHEN metric = ? THEN 'selected' " +
//         "         WHEN (\"" + endHD + "\" - \"" + startHD + "\") > 0 THEN 'positive' " +
//         "         WHEN (\"" + endHD + "\" - \"" + startHD + "\") < 0 THEN 'negative' ELSE 'neutral' END AS trend " +
//         "  FROM Pivoted " +
//         "), RefMetric AS ( " +
//         "  SELECT percent_change AS ref_pc FROM ChangeAndTrend WHERE metric = ? " +
//         "), Ranked AS ( " +
//         "  SELECT ct.*, ABS(ct.percent_change - rm.ref_pc) AS dist FROM ChangeAndTrend ct CROSS JOIN RefMetric rm " +
//         "), Final AS ( " +
//         "  SELECT " + selectCols + ", percent_change, trend, dist FROM Ranked WHERE metric = ? " +
//         "  UNION ALL " +
//         "  SELECT " + selectCols + ", percent_change, trend, dist FROM Ranked WHERE metric != ? ORDER BY dist LIMIT 4 " +
//         ") SELECT * FROM Final;";

//     try (Connection conn = DriverManager.getConnection(DATABASE);
//          PreparedStatement ps = conn.prepareStatement(query)) {

//         ps.setInt(1, stationId);
//         ps.setInt(2, startYear);
//         ps.setInt(3, endYear);
//         ps.setInt(4, endHD);
//         ps.setInt(5, startHD);
//         ps.setString(6, referenceMetric);
//         ps.setString(7, referenceMetric);
//         ps.setString(8, referenceMetric);
//         ps.setString(9, referenceMetric);

//         ResultSet rs = ps.executeQuery();
//         int colCount = rs.getMetaData().getColumnCount();
//         while (rs.next()) {
//             ArrayList<String> row = new ArrayList<>();
//             for (int i = 1; i <= colCount; i++) {
//                 row.add(rs.getString(i));
//             }
//             results.add(row);
//         }
//     } catch (SQLException e) {
//         System.err.println("get3CAnalysisData SQL error: " + e.getMessage());
//     }

//     return results;
// }

    public ArrayList<ArrayList<String>> get3CAnalysisData(int stationId, int startYear, int endYear, String referenceMetric) {
    ArrayList<ArrayList<String>> results = new ArrayList<>();
    StringBuilder selectCols = new StringBuilder("metric");
    StringBuilder pivotExprs = new StringBuilder();

    int startHD, endHD;

    // ✅ Adjusted logic for ranges less than 5 years
    if ((endYear - startYear) < 5) {
        startHD = (startYear / 5) * 5;
        endHD = startHD; // Only one half-decade
    } else {
        startHD = (startYear / 5) * 5;
        endHD = ((endYear + 4) / 5) * 5;
    }

    // Build dynamic half-decade columns
    for (int hd = startHD; hd <= endHD; hd += 5) {
        selectCols.append(", \"").append(hd).append("\"");
        pivotExprs.append(", MAX(CASE WHEN halfdecade = '").append(hd)
                  .append("' THEN avg_value END) AS \"").append(hd).append("\"");
    }

    // SQL using dynamic columns and correct percent_change calculation
    String query =
        "WITH HalfDecadeData AS ( " +
        "  SELECT metric, dt.halfdecade, AVG(cm.value) AS avg_value " +
        "  FROM ClimateComparisonValue cm JOIN DateTime dt ON cm.date = dt.date " +
        "  WHERE cm.stationID = ? AND CAST(strftime('%Y', cm.date) AS INTEGER) BETWEEN ? AND ? AND cm.value IS NOT NULL " +
        "  GROUP BY metric, dt.halfdecade " +
        "), Pivoted AS ( " +
        "  SELECT metric" + pivotExprs + " FROM HalfDecadeData GROUP BY metric " +
        "), ChangeAndTrend AS ( " +
        "  SELECT *, " +
        "    CASE WHEN \"" + startHD + "\" = 0 THEN 0 " +
        "         ELSE ROUND( ( (\"" + endHD + "\" - \"" + startHD + "\") / \"" + startHD + "\") / (? - ?) * 100.0, 2 ) END AS percent_change, " +
        "    CASE WHEN metric = ? THEN 'selected' " +
        "         WHEN (\"" + endHD + "\" - \"" + startHD + "\") > 0 THEN 'positive' " +
        "         WHEN (\"" + endHD + "\" - \"" + startHD + "\") < 0 THEN 'negative' ELSE 'neutral' END AS trend " +
        "  FROM Pivoted " +
        "), RefMetric AS ( " +
        "  SELECT percent_change AS ref_pc FROM ChangeAndTrend WHERE metric = ? " +
        "), Ranked AS ( " +
        "  SELECT ct.*, ABS(ct.percent_change - rm.ref_pc) AS dist FROM ChangeAndTrend ct CROSS JOIN RefMetric rm " +
        "), Final AS ( " +
        "  SELECT " + selectCols + ", percent_change, trend, dist FROM Ranked WHERE metric = ? " +
        "  UNION ALL " +
        "  SELECT " + selectCols + ", percent_change, trend, dist FROM Ranked WHERE metric != ? ORDER BY dist LIMIT 4 " +
        ") SELECT * FROM Final;";

    try (Connection conn = DriverManager.getConnection(DATABASE);
         PreparedStatement ps = conn.prepareStatement(query)) {

        ps.setInt(1, stationId);
        ps.setInt(2, startYear);
        ps.setInt(3, endYear);
        ps.setInt(4, endHD);
        ps.setInt(5, startHD);
        ps.setString(6, referenceMetric);
        ps.setString(7, referenceMetric);
        ps.setString(8, referenceMetric);
        ps.setString(9, referenceMetric);

        ResultSet rs = ps.executeQuery();
        int colCount = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            ArrayList<String> row = new ArrayList<>();
            for (int i = 1; i <= colCount; i++) {
                row.add(rs.getString(i));
            }
            results.add(row);
        }
    } catch (SQLException e) {
        System.err.println("get3CAnalysisData SQL error: " + e.getMessage());
    }

    return results;
}





    

    //TODO: Add your required methods here
        public String getPersonaInformation(int id, String columnName) {
        String information = "";
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE);
            PreparedStatement ps = connection.prepareStatement("SELECT " + columnName + " FROM Personas WHERE id=?");
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                information = rs.getString(columnName);
            }
            
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return information;
    }


    public String getMemberInformation(int id, String columnName) {
        String information = "";
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE);
            PreparedStatement ps = connection.prepareStatement("select " + columnName + " from GroupMembers where id=?");
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                information = rs.getString(columnName);
            }
            
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return information;
    }


    public List<String> getClimateMetrics(){
        List<String> climateMetrics = new ArrayList<String>();
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            //Change table name and column name
            String query = "SELECT field FROM Metadata WHERE field NOT LIKE '%Qual' AND field NOT LIKE '%Num' AND field NOT LIKE '%Days%' AND field != 'Location' AND field != 'DMY';";
            ResultSet  results = statement.executeQuery(query);

            while (results.next()) {
                String metric = results.getString("field");
                climateMetrics.add(metric);
            }

        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return climateMetrics;
        
    }
    
    
    
    public ArrayList<String> getStationIds(){
        ArrayList<String> stationIdsAndNames = new ArrayList<String>();
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT site, name FROM Location";
            ResultSet  results = statement.executeQuery(query);

            while (results.next()) {
                String site = results.getString("site");
                String name = results.getString("name");
                String siteAndName = site + " " + name;
                stationIdsAndNames.add(siteAndName);
            }

        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return stationIdsAndNames;
        
    }
    
    public ArrayList<String> getSitesAndStates(String startSite, String endSite) {
        ArrayList<String> sitesAndStates = new ArrayList<String>();
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE);
            PreparedStatement ps = connection.prepareStatement("SELECT site, state FROM Location WHERE site >= ? AND site <= ?");
            ps.setString(1, startSite);
            ps.setString(2, endSite);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String site = rs.getString("site");
                String state = rs.getString("state");
                sitesAndStates.add(site);
                sitesAndStates.add(state);
            }
            
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return sitesAndStates;
    }

    public ArrayList<String> getDailyValues(String climateMetric, String site, String state) {
        ArrayList<String> dailyValues = new ArrayList<String>();
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE);
            PreparedStatement ps = connection.prepareStatement("SELECT date, ? FROM ? WHERE site = ?");
            ps.setString(1, climateMetric);
            ps.setString(2, state);
            ps.setString(3, site);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String date = rs.getString("date");
                String metricValue = rs.getString(climateMetric);
                dailyValues.add(date);
                dailyValues.add(metricValue);
            }
            
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return dailyValues;
    }

   public ArrayList<String> getDailyValues(String metric, int fromStationId, int toStationId, String startDate, String endDate) {
        ArrayList<String> dailyValues = new ArrayList<String>();
        Connection connection = null;
        String query = "SELECT stationId, DMY, ROUND(" + metric + ", 2) AS " + metric + " FROM ClimateData WHERE stationId >= ? AND stationId <= ? AND DMY >= ? AND DMY <= ?;";
        System.out.println(query);

        try {
            connection = DriverManager.getConnection(DATABASE);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, fromStationId);
            ps.setInt(2, toStationId);
            ps.setString(3, startDate);
            ps.setString(4, endDate);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String site = String.valueOf(rs.getInt("stationId"));
                String date = rs.getString("DMY");
                String climateMetric = rs.getString(metric);
                dailyValues.add(site);
                dailyValues.add(date);
                dailyValues.add(climateMetric);
            } 
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return dailyValues;
   }

   public ArrayList<String> getSummaryTable(String climateMetric, int fromStationINT, int toStationINT, String startDate, String endDate, List<String> timeFilter,  List<String> locationFilter, String summaryType, String orderBy) {
    ArrayList<String> summaryTableValues = new ArrayList<String>();
    Connection connection = null;
    String query = "";
    int i;

    try {
        connection = DriverManager.getConnection(DATABASE);
        query = "SELECT ";

        if (orderBy.compareToIgnoreCase("Time") == 0) {
            for (i = 0; i < timeFilter.size(); ++i) {
                query += "dt." + timeFilter.get(i) + ", ";
            } 
            for (i = 0; i < locationFilter.size(); ++i) {
                query += "l." + locationFilter.get(i) + ", ";
            } 
            query += "ROUND(" + summaryType + "(cd." + climateMetric + "), 2) AS \"" + summaryType + " " + climateMetric + "\"";
            query += " FROM ClimateData AS cd JOIN DateTime AS dt ON cd.DMY = dt.Date JOIN Location AS l ON l.site=cd.stationId WHERE cd.stationId >= " + fromStationINT + " AND cd.stationId <= " + toStationINT + " AND cd.DMY >= '" + startDate + "' AND cd.DMY <= '" + endDate + "' GROUP BY ";
            for (i = 0; i < timeFilter.size(); ++i) {
                query += "dt." + timeFilter.get(i) + ", ";
            } 
            for (i = 0; i < locationFilter.size(); ++i) {
                query += "l." + locationFilter.get(i);
                if (i != locationFilter.size() - 1) {
                    query += ", ";
                }
                else {
                    query += " ";
                }
            }
            for (i = 0; i < timeFilter.size(); ++i) {
                if (timeFilter.get(i).compareTo("Month") == 0) {
                    query += "ORDER BY dt.MonthNum;";
                    break;
                }
            }
        }
            
        else if (orderBy.compareToIgnoreCase("Location") == 0) {
            for (i = 0; i < locationFilter.size(); ++i) {
                query += "l." + locationFilter.get(i) + ", ";
            } 
            for (i = 0; i < timeFilter.size(); ++i) {
                query += "dt." + timeFilter.get(i) + ", ";                } 
                query += "ROUND(" + summaryType + "(" + climateMetric + "), 2) AS \"" + summaryType + " " + climateMetric + "\"";
                query += " FROM ClimateData AS cd JOIN DateTime AS dt ON cd.DMY = dt.Date JOIN Location AS l ON l.site=cd.stationId WHERE cd.stationId >= " + fromStationINT + " AND cd.stationId <= " + toStationINT + " AND cd.DMY >= '" + startDate + "' AND cd.DMY <= '" + endDate + "' GROUP BY ";
            for (i = 0; i < locationFilter.size(); ++i) {
                query += "l." + locationFilter.get(i) + ", ";
            } 
            for (i = 0; i < timeFilter.size(); ++i) {                    
                query += "dt." + timeFilter.get(i);
                if (i != timeFilter.size() - 1) {
                    query += ", ";
                }
                else {
                    query += " ";
                }   
            }
            for (i = 0; i < timeFilter.size(); ++i) {
                if (timeFilter.get(i).compareTo("Month") == 0) {
                    query += "ORDER BY dt.MonthNum;";
                    break;
                }
            }
        }

        System.out.println(query);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);
        
        ResultSet rs = statement.executeQuery(query);
        if (orderBy.compareToIgnoreCase("Time") == 0) {
            while (rs.next()) {
                for (i = 0; i < timeFilter.size(); ++i) {
                    summaryTableValues.add(rs.getString(timeFilter.get(i)));
                }
                for (i = 0; i < locationFilter.size(); ++i) {
                    summaryTableValues.add(rs.getString(locationFilter.get(i)));
                }
                summaryTableValues.add(rs.getString(summaryType + " " + climateMetric));
            }
        } 
    
    } catch (SQLException e) {
        // If there is an error, lets just pring the error
        System.err.println(e.getMessage());
    } finally {
        // Safety code to cleanup
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            // connection close failed.
            System.err.println(e.getMessage());
        }
    }

    return summaryTableValues;
  }






   //Level 3B

   //Getting grouped time periods for table 2: 3B (eg ; 2019 H1 2019 H2)
   public ArrayList<String> getGroupedTimePeriods(String startDate, String endDate, List<String> groupBy) {
    ArrayList<String> groupedTimePeriods = new ArrayList<String>();

    String query = "SELECT ";
    for (int i = 0; i < groupBy.size(); ++i) {
        if (i != groupBy.size() - 1) {
            query += groupBy.get(i) + ", ";
        }
        else {
            query += groupBy.get(i) + " ";
        }
    }
    query += "FROM DateTime WHERE Date >= \'" + startDate + "\' AND Date <= \'" + endDate + "\' GROUP BY ";
    for (int i = 0; i < groupBy.size(); ++i) {
        if (i != groupBy.size() - 1) {
            query += groupBy.get(i) + ", ";
        }
        else {
            query += groupBy.get(i) + " ";
        }
    }
    for (int i = 0; i < groupBy.size(); ++i) {
        if (groupBy.get(i).compareTo("Month") == 0) {
            query += "ORDER BY MonthNum;";
            break;
        }
    }

    Connection connection = null;

    try {
        connection = DriverManager.getConnection(DATABASE);

        System.out.println(query);
        System.out.println();

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);
        
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            for (int i = 0; i < groupBy.size(); ++i) {
                groupedTimePeriods.add(rs.getString(groupBy.get(i)));
            }
        } 
    } catch (SQLException e) {
        // If there is an error, lets just pring the error
        System.err.println(e.getMessage());
    } finally {
        // Safety code to cleanup
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            // connection close failed.
            System.err.println(e.getMessage());
        }
    }

    return groupedTimePeriods;
   }


   //Query for getting values for each separate metric table 1 and 2: 3B
   public String getQueryfor3B(String metric, String dataType, String startDate, String endDate, List<String> groupBy) {
    int i;

    String query = "SELECT ";
    for (i = 0; i < groupBy.size(); ++i) {
        query += "dt." + groupBy.get(i) + ", ";
    }
    query += dataType + "(CAST(cd." + metric + " AS REAL)) AS \"" + dataType + " " + metric + "\"";
    query += " FROM ClimateData AS cd JOIN DateTime AS dt ON cd.DMY = dt.Date  WHERE cd.DMY >= '" + startDate + "' AND cd.DMY <= '" + endDate + "' GROUP BY ";
    for (i = 0; i < groupBy.size(); ++i) {
        query += "dt." + groupBy.get(i);
        if (i != groupBy.size() - 1) {
            query += ", ";
        }
        else {
            query += " ";
        }
    }
    for (i = 0; i < groupBy.size(); ++i) {
        if (groupBy.get(i).compareTo("Month") == 0) {
            query += "ORDER BY dt.MonthNum;";
            break;
        }
    }
    return query;
   }



   //Making ArrayList<Metric> for each Metric Object, adding metricName and Values
   public ArrayList<Metric> getMetricComparisonData(List<String> metrics, String dataType, String startDate, String endDate, List<String> groupBy) {
    ArrayList<Metric> allMetrics = new ArrayList<Metric>();
    Connection connection = null;
    String query = "";
    int i;

    for (i = 0; i < metrics.size(); ++i) {
        query = getQueryfor3B(metrics.get(i), dataType, startDate, endDate, groupBy);
        System.out.println(query);
        ArrayList<Double> values = new ArrayList<Double>();
        try {
            connection = DriverManager.getConnection(DATABASE);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                values.add(rs.getDouble(dataType + " " + metrics.get(i)));
            } 
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        Metric metric = new Metric();
        metric.setMetricName(metrics.get(i));
        metric.setValues(values);

        allMetrics.add(metric);
    }

    return allMetrics;
  }

  





    public ArrayList<String> getAverageTemperatureByYear() {
        ArrayList<String> temperatureData = new ArrayList<String>();
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = """
                SELECT 
                    strftime('%Y', DMY) as Year,
                    ROUND(AVG(CAST(MaxTemp AS FLOAT)), 2) as AvgTemp
                FROM ClimateData
                WHERE MaxTemp != ''
                GROUP BY strftime('%Y', DMY)
                HAVING Year BETWEEN '1970' AND '2020'
                ORDER BY Year
            """;
            
            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                String year = results.getString("Year");
                String avgTemp = results.getString("AvgTemp");
                temperatureData.add(year);
                temperatureData.add(avgTemp);
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return temperatureData;
    }
}
