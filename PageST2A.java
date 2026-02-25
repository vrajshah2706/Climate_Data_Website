package app;

import java.util.ArrayList;
import java.util.List;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin
 * by writing the raw HTML into a Java String object
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class PageST2A implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page2A.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html = html + "<head>" + 
                "<title>Subtask 2.A</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        html = html + "<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css'>";
        html = html + "</head>";

        // Add the body
        html = html + "<body>";

        // Add the topnav
        // This uses a Java v15+ Text Block
        html = html + """
                <div class='topnav'>
                    <a href='/' class='site-title'>Climate Change</a>
                    <div class='nav-items'>
                        <a href='/'>Home</a>
                        <a href='mission.html'>Our Mission</a>
                        <a href="equipment.html">Dataset Summary</a>
                        <div class="dropdown">
                            <button class="dropbtn">Explore 
                                <i class="fa fa-caret-down"></i>
                            </button>
                            <div class="dropdown-content">
                                <a href='page2A.html'>Weather Stations</a>
                                <a href='page2B.html'>Climate Metrics</a>
                                <a href='page2C.html'>Climate Data Quality</a>
                                <a href='page3A.html'>Weather Station Similarities</a>
                                <a href='page3B.html'>Climate Metric Similarities</a>
                                <a href='page3C.html'>Impact of Metrics</a>
                            </div>
                        </div>
                    </div>
                </div>
            """;

        // Add header content block
        html = html + """
            <div class='header'>
                <h1>View by Weather Stations</h1>
            </div>
        """;

        // Add Div for page Content
        html = html + "<div class='content'>";

        // data-border (for the background for the data table)

        // Add HTML for the page content
        html = html + """
                <div class='filterGroup'>
                    <form action='/page2A.html' method='post' id='filterForm'>
                        <div class='filter-row'>
                            <div class='filter-item'>
                                <label for='state'>State:</label>
                                <select id='state' name='state' required>
                                    <option value='VIC'>Victoria</option>
                                    <option value='N.S.W.'>New South Wales</option>
                                    <option value='QLD'>Queensland</option>
                                    <option value='S.A.'>South Australia</option>
                                    <option value='W.A.'>Western Australia</option>
                                    <option value='TAS'>Tasmania</option>
                                    <option value='N.T.'>Northern Territory</option>
                                    <option value='A.C.T.'>Australian Capital Territory</option>
                                </select>
                            </div>

                            <div class='filter-item'>
                                <label for='startLat'>Starting Latitude:</label>
                                <input type='number' id='startLat' name='startLat' step='0.000001' placeholder='Enter latitude' required>
                            </div>

                            <div class='filter-item'>
                                <label for='endLat'>Ending Latitude:</label>
                                <input type='number' id='endLat' name='endLat' step='0.000001' placeholder='Enter latitude' required>
                            </div>

                            <div class='filter-item'>
                                <label for='climateMetric'>Climate Metric:</label>
                                <select id='climateMetric' name='climateMetric' required>
                                    <!-- Dynamically populate options from database -->
                                    """;
                                        JDBCConnection jdbcForMetrics = new JDBCConnection();
                                        List<String> climateMetricsList = jdbcForMetrics.getClimateMetrics();
                                        for (String metric : climateMetricsList) {
                                            html += "<option value='" + metric + "'>" + metric + "</option>";
                                        }
                                        html += """
                                </select>
                            </div>

                            <div class='filter-item'>
                                <label for='sortBy'>Sort By:</label>
                                <select id='sortBy' name='sortBy' required>
                                    <option value='stationid'>Station ID</option>
                                    <option value='name'>Station Name</option>
                                    <option value='latitude'>Latitude</option>
                                    <option value='metric'>Climate Metric</option>
                                </select>
                            </div>

                            <div class='filter-item'>
                                <button type='submit' class='btn btn-primary'>Apply Filters</button>
                            </div>

                        </div>
                    </form>
                </div>
                
                <script>
                // Save form values to localStorage
                function saveFormValues() {
                    const form = document.getElementById('filterForm');
                    const formData = new FormData(form);
                    const values = {};
                    
                    for (let [key, value] of formData.entries()) {
                        values[key] = value;
                    }
                    
                    localStorage.setItem('st2a_filters', JSON.stringify(values));
                }
                
                // Restore form values from localStorage
                function restoreFormValues() {
                    const saved = localStorage.getItem('st2a_filters');
                    if (saved) {
                        const values = JSON.parse(saved);
                        
                        // Restore select values
                        if (values.state) document.getElementById('state').value = values.state;
                        if (values.climateMetric) document.getElementById('climateMetric').value = values.climateMetric;
                        if (values.sortBy) document.getElementById('sortBy').value = values.sortBy;
                        
                        // Restore input values
                        if (values.startLat) document.getElementById('startLat').value = values.startLat;
                        if (values.endLat) document.getElementById('endLat').value = values.endLat;
                    }
                }
                
                // Add event listeners to save values when form changes
                document.addEventListener('DOMContentLoaded', function() {
                    restoreFormValues();
                    
                    const form = document.getElementById('filterForm');
                    const inputs = form.querySelectorAll('input, select');
                    
                    inputs.forEach(input => {
                        input.addEventListener('change', saveFormValues);
                    });
                });
                </script>
            """;

        // Get filter values
        String state = context.formParam("state");
        String startLatStr = context.formParam("startLat");
        String endLatStr = context.formParam("endLat");
        String climateMetric = context.formParam("climateMetric");
        String sortBy = context.formParam("sortBy");

        Double startLat = null;
        Double endLat = null;
        if (startLatStr != null && !startLatStr.isEmpty()) {
            try { startLat = Double.parseDouble(startLatStr); } catch (NumberFormatException e) { startLat = null; }
        }
        if (endLatStr != null && !endLatStr.isEmpty()) {
            try { endLat = Double.parseDouble(endLatStr); } catch (NumberFormatException e) { endLat = null; }
        }

        if (state != null && startLat != null && endLat != null && climateMetric != null) {
            JDBCConnection jdbc = new JDBCConnection();
            ArrayList<String> stationIds = jdbc.getStationIds();
            ArrayList<String[]> filteredStations = new ArrayList<>();
            // Filter stations by state and latitude
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(JDBCConnection.DATABASE);
                String sql = "SELECT Site, Name, Lat FROM Location WHERE State = ? AND CAST(Lat AS REAL) >= ? AND CAST(Lat AS REAL) <= ?";
                java.sql.PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, state);
                ps.setDouble(2, Math.min(startLat, endLat));
                ps.setDouble(3, Math.max(startLat, endLat));
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String site = rs.getString("Site");
                    String name = rs.getString("Name");
                    String lat = rs.getString("Lat");
                    filteredStations.add(new String[]{site, name, lat});
                }
                rs.close();
                ps.close();
            } catch (SQLException e) {
                html += "<p>Error querying locations: " + e.getMessage() + "</p>";
            } finally {
                try { if (connection != null) connection.close(); } catch (SQLException e) {}
            }

            if (filteredStations.size() == 0) {
                html += "<p>No stations found for the selected filters.</p>";
            } else {
                // Prepare a list of rows with metric value for sorting
                ArrayList<String[]> tableRows = new ArrayList<>();
                for (String[] station : filteredStations) {
                    String site = station[0];
                    String name = station[1];
                    String lat = station[2];
                    // Get the latest value for the selected metric
                    String metricValue = "N/A";
                    Connection conn2 = null;
                    try {
                        conn2 = DriverManager.getConnection(JDBCConnection.DATABASE);
                        Statement st2 = conn2.createStatement();
                        String q = "SELECT " + climateMetric + " FROM ClimateData WHERE stationId = '" + site + "' AND " + climateMetric + " != '' ORDER BY DMY DESC LIMIT 1";
                        ResultSet rs2 = st2.executeQuery(q);
                        if (rs2.next()) {
                            metricValue = rs2.getString(climateMetric);
                        }
                        rs2.close();
                        st2.close();
                    } catch (SQLException e) {
                        metricValue = "Error";
                    } finally {
                        try { if (conn2 != null) conn2.close(); } catch (SQLException e) {}
                    }
                    tableRows.add(new String[]{site, name, lat, metricValue});
                }
                // Sort the tableRows based on sortBy
                if (sortBy != null) {
                    switch (sortBy) {
                        case "stationid":
                            tableRows.sort((a, b) -> a[0].compareTo(b[0]));
                            break;
                        case "name":
                            tableRows.sort((a, b) -> a[1].compareToIgnoreCase(b[1]));
                            break;
                        case "latitude":
                            tableRows.sort((a, b) -> {
                                try {
                                    return Double.compare(Double.parseDouble(a[2]), Double.parseDouble(b[2]));
                                } catch (Exception e) { return 0; }
                            });
                            break;
                        case "metric":
                            tableRows.sort((a, b) -> {
                                try {
                                    return Double.compare(Double.parseDouble(a[3]), Double.parseDouble(b[3]));
                                } catch (Exception e) { return a[3].compareTo(b[3]); }
                            });
                            break;
                    }
                }
                html += "<div style='display: flex; justify-content: center; gap: 2em; margin-top: 2em;'>";
                html += "<div class='data-border' style='display: flex; flex-direction: column; align-items: center;'>";
                html += "<h3 style='text-align: center; margin-bottom: 1em;'>Stations</h3>";
                html += "<table><tr><th>Station ID</th><th>Name</th><th>Latitude</th><th>" + climateMetric + "</th></tr>";
                for (String[] row : tableRows) {
                    html += "<tr><td>" + row[0] + "</td><td>" + row[1] + "</td><td>" + row[2] + "</td><td>" + row[3] + "</td></tr>";
                }
                html += "</table></div>";

                // --- Region Grouped Table ---
                // Map: region -> [sum, count]
                java.util.Map<String, double[]> regionMetric = new java.util.HashMap<>();
                java.util.Map<String, Integer> regionStationCount = new java.util.HashMap<>();
                // First, get all regions for the filtered stations
                for (String[] station : filteredStations) {
                    String site = station[0];
                    String region = "";
                    Connection conn3 = null;
                    try {
                        conn3 = DriverManager.getConnection(app.JDBCConnection.DATABASE);
                        java.sql.PreparedStatement ps = conn3.prepareStatement("SELECT Region FROM Location WHERE Site = ?");
                        ps.setString(1, site);
                        java.sql.ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            region = rs.getString("Region");
                        }
                        rs.close();
                        ps.close();
                    } catch (Exception e) { region = "Unknown"; }
                    finally { try { if (conn3 != null) conn3.close(); } catch (Exception e) {} }

                    // Get the latest value for the selected metric
                    String metricValue = null;
                    Connection conn4 = null;
                    try {
                        conn4 = DriverManager.getConnection(app.JDBCConnection.DATABASE);
                        java.sql.Statement st2 = conn4.createStatement();
                        String q = "SELECT " + climateMetric + " FROM ClimateData WHERE stationId = '" + site + "' AND " + climateMetric + " != '' ORDER BY DMY DESC LIMIT 1";
                        java.sql.ResultSet rs2 = st2.executeQuery(q);
                        if (rs2.next()) {
                            metricValue = rs2.getString(climateMetric);
                        }
                        rs2.close();
                        st2.close();
                    } catch (Exception e) { metricValue = null; }
                    finally { try { if (conn4 != null) conn4.close(); } catch (Exception e) {} }

                    if (region != null && !region.isEmpty()) {
                        double[] sumCount = regionMetric.getOrDefault(region, new double[]{0.0, 0.0});
                        if (metricValue != null && !metricValue.equals("N/A") && !metricValue.equals("Error")) {
                            try {
                                double val = Double.parseDouble(metricValue);
                                sumCount[0] += val;
                                sumCount[1] += 1.0;
                            } catch (Exception e) {}
                        }
                        regionMetric.put(region, sumCount);
                        regionStationCount.put(region, regionStationCount.getOrDefault(region, 0) + 1);
                    }
                }

                html += "<div class='data-border' style='display: flex; flex-direction: column; align-items: center;'>";
                html += "<h3 style='text-align: center; margin-bottom: 1em;'>Regions</h3>";
                html += "<table><tr><th>Region</th><th>Average " + climateMetric + "</th><th>Station Count</th></tr>";
                for (String region : regionMetric.keySet()) {
                    double[] sumCount = regionMetric.get(region);
                    int count = regionStationCount.get(region);
                    String avg = (sumCount[1] > 0) ? String.format("%.2f", sumCount[0] / sumCount[1]) : "N/A";
                    html += "<tr><td>" + region + "</td><td>" + avg + "</td><td>" + count + "</td></tr>";
                }
                html += "</table></div>";
                html += "</div>";
            }
        } else {
            html += "<p>Please select all filters and enter valid latitude values.</p>";
        }

        // Close Content div
        html = html + "</div>";

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";
        

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

}
