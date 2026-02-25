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
public class PageST3A implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page3A.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html = html + "<head>" + 
                "<title>Weather Station Similarities</title>";

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
                <h1>Weather Station Similarities</h1>
            </div>
        """;

        // Add Div for page Content
        html = html + "<div class='content'>";

        // Add HTML for the page content
        html = html + """
            <div class='filterGroup'>
                <form action='/page3A.html' method='post' id='filterForm'>
                    <div class='filter-row'>
                        <div class='filter-item'>
                            <label for='startDate'>Start Date:</label>
                            <input type='date' id='startDate' name='startDate' min='1970-01-01' max='2020-12-31' required>
                        </div>

                        <div class='filter-item'>
                            <label for='endDate'>End Date:</label>
                            <input type='date' id='endDate' name='endDate' min='1970-01-01' max='2020-12-31' required>
                        </div>

                        <div class='filter-item'>
                            <label for='period'>Period:</label>
                            <select id='period' name='period' required>
                                <option value='monthly'>Monthly</option>
                                <option value='quarterly'>Quarterly</option>
                                <option value='yearly'>Yearly</option>
                                <option value='half-decades'>Half-Decades</option>
                                <option value='decades'>Decades</option>
                            </select>
                        </div>

                        <div class='filter-item'>
                            <label for='referenceStation'>Reference Station:</label>
                            <select id='referenceStation' name='referenceStation' required>
                                <option value=''>Select a station</option>
            """;
        
        // Populate station dropdown
        JDBCConnection jdbc = new JDBCConnection();
        List<String> stationIds = jdbc.getStationIds();
        for (String stationId : stationIds) {
            html = html + "<option value='" + stationId + "'>" + stationId + "</option>";
        }

        html = html + """
                            </select>
                        </div>

                        <div class='filter-item'>
                            <label for='numStations'>Number of Stations to Find:</label>
                            <input type='number' id='numStations' name='numStations' min='1' max='50' value='10' required>
                        </div>

                        <div class='filter-item'>
                            <label for='climateMetric'>Climate Metric:</label>
                            <select id='climateMetric' name='climateMetric' required>
                                <option value=''>Select a metric</option>
            """;
        
        // Populate climate metrics dropdown
        List<String> climateMetrics = jdbc.getClimateMetrics();
        for (String metric : climateMetrics) {
            html = html + "<option value='" + metric + "'>" + metric + "</option>";
        }

        html = html + """
                            </select>
                        </div>

                        <div class='filter-item'>
                            <button type='submit' class='btn btn-primary'>Find Similar Stations</button>
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
                
                localStorage.setItem('st3a_filters', JSON.stringify(values));
            }
            
            // Restore form values from localStorage
            function restoreFormValues() {
                const saved = localStorage.getItem('st3a_filters');
                if (saved) {
                    const values = JSON.parse(saved);
                    
                    // Restore select values
                    if (values.period) document.getElementById('period').value = values.period;
                    if (values.referenceStation) document.getElementById('referenceStation').value = values.referenceStation;
                    if (values.climateMetric) document.getElementById('climateMetric').value = values.climateMetric;
                    
                    // Restore input values
                    if (values.startDate) document.getElementById('startDate').value = values.startDate;
                    if (values.endDate) document.getElementById('endDate').value = values.endDate;
                    if (values.numStations) document.getElementById('numStations').value = values.numStations;
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

        // Process form data and display results
        String startDate = context.formParam("startDate");
        String endDate = context.formParam("endDate");
        String period = context.formParam("period");
        String referenceStation = context.formParam("referenceStation");
        String numStationsStr = context.formParam("numStations");
        String climateMetric = context.formParam("climateMetric");

        if (startDate != null && endDate != null && period != null && referenceStation != null && 
            numStationsStr != null && climateMetric != null) {
            
            int numStations = Integer.parseInt(numStationsStr);
            
            // Extract station ID from reference station (format: "ID Name")
            String refStationId = referenceStation.split(" ")[0];
            
            try {
                // Get all stations with their averages
                List<StationData> allStations = getAllStationsAverages(climateMetric, startDate, endDate, period);
                List<String> periods = getPeriods(startDate, endDate, period);
                
                if (allStations.isEmpty() || periods.isEmpty()) {
                    html += "<p style='text-align: center; color: red;'>No data found for the selected criteria.</p>";
                } else {
                    // Find reference station data
                    StationData refStation = null;
                    for (StationData station : allStations) {
                        if (station.stationId.equals(refStationId)) {
                            refStation = station;
                            break;
                        }
                    }
                    
                    if (refStation == null) {
                        html += "<p style='text-align: center; color: red;'>Reference station not found in data.</p>";
                    } else {
                        // Calculate similarities and sort by difference from reference
                        for (StationData station : allStations) {
                            if (!station.stationId.equals(refStationId)) {
                                // Calculate overall difference for ranking
                                double overallDiff = 0;
                                int validPeriods = 0;
                                for (String p : periods) {
                                    double refVal = refStation.periodAverages.getOrDefault(p, 0.0);
                                    double stationVal = station.periodAverages.getOrDefault(p, 0.0);
                                    if (refVal != 0.0 && stationVal != 0.0) {
                                        overallDiff += Math.abs((stationVal - refVal) / refVal) * 100;
                                        validPeriods++;
                                    }
                                }
                                station.difference = validPeriods > 0 ? overallDiff / validPeriods : 100.0;
                            }
                        }
                        
                        // Sort by difference (closest first) and take top N
                        allStations.sort((a, b) -> Double.compare(a.difference, b.difference));
                        List<StationData> similarStations = allStations.subList(0, Math.min(numStations, allStations.size()));
                        
                        // Display results table
                        html += "<div class='data-border' style='display: flex; justify-content: center; margin-top: 2em;'>";
                        html += "<table>";
                        html += "<tr><th>Station Name</th>";
                        for (String p : periods) {
                            html += "<th>" + p + " " + climateMetric + "</th>";
                        }
                        html += "<th>% Change</th><th>Difference from Reference</th></tr>";
                        
                        // Add reference station row
                        html += "<tr style='background-color: #e6f3ff;'>";
                        html += "<td><strong>" + referenceStation + " (Reference)</strong></td>";
                        for (String p : periods) {
                            double refVal = refStation.periodAverages.getOrDefault(p, 0.0);
                            html += "<td><strong>" + String.format("%.2f", refVal) + "</strong></td>";
                        }
                        html += "<td><strong>0.00%</strong></td><td><strong>0.00%</strong></td>";
                        html += "</tr>";
                        
                        // Add similar stations
                        for (StationData station : similarStations) {
                            if (!station.stationId.equals(refStationId)) {
                                html += "<tr>";
                                html += "<td>" + station.stationName + "</td>";
                                
                                // Calculate percentage change between periods for this station
                                double totalPercentChange = 0;
                                int validPeriods = 0;
                                String prevPeriod = null;
                                double prevValue = 0;
                                
                                for (String p : periods) {
                                    double val = station.periodAverages.getOrDefault(p, 0.0);
                                    html += "<td>" + String.format("%.2f", val) + "</td>";
                                    
                                    if (prevPeriod != null && prevValue != 0.0 && val != 0.0) {
                                        double periodChange = ((val - prevValue) / prevValue) * 100;
                                        totalPercentChange += periodChange;
                                        validPeriods++;
                                    }
                                    prevPeriod = p;
                                    prevValue = val;
                                }
                                
                                double avgPercentChange = validPeriods > 0 ? totalPercentChange / validPeriods : 0;
                                
                                html += "<td>" + String.format("%.2f", avgPercentChange) + "%</td>";
                                html += "<td>" + String.format("%.2f", station.difference) + "%</td>";
                                html += "</tr>";
                            }
                        }
                        
                        html += "</table>";
                        html += "</div>";
                    }
                }
            } catch (Exception e) {
                html += "<p style='text-align: center; color: red;'>Error processing data: " + e.getMessage() + "</p>";
                System.err.println("Error in PageST3A: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Close Content div
        html = html + "</div>";

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";
        

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }
    
    private List<StationData> getAllStationsAverages(String metric, String startDate, String endDate, String period) {
        List<StationData> stations = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);
            String periodSelect = getPeriodSelectClause(period);
            String query = "SELECT l.site, l.name, " + periodSelect + " as period_value, " +
                          "AVG(CAST(cd." + metric + " AS FLOAT)) as avg_value " +
                          "FROM Location l " +
                          "JOIN ClimateData cd ON l.site = cd.stationId " +
                          "WHERE cd.DMY BETWEEN ? AND ? AND cd." + metric + " != '' " +
                          "GROUP BY l.site, l.name, " + periodSelect + " " +
                          "HAVING avg_value IS NOT NULL";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String stationId = rs.getString("site");
                String stationName = rs.getString("name");
                String periodValue = rs.getString("period_value");
                double avgValue = rs.getDouble("avg_value");
                
                if (stationId != null && stationName != null && periodValue != null) {
                    // Find or create station data
                    StationData station = null;
                    for (StationData s : stations) {
                        if (s.stationId.equals(stationId)) {
                            station = s;
                            break;
                        }
                    }
                    if (station == null) {
                        station = new StationData();
                        station.stationId = stationId;
                        station.stationName = stationName;
                        station.periodAverages = new java.util.HashMap<>();
                        stations.add(station);
                    }
                    
                    station.periodAverages.put(periodValue, avgValue);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting all stations averages: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (connection != null) connection.close(); } catch (SQLException e) {}
        }
        return stations;
    }
    
    private List<String> getPeriods(String startDate, String endDate, String period) {
        List<String> periods = new ArrayList<>();
        try {
            java.time.LocalDate start = java.time.LocalDate.parse(startDate);
            java.time.LocalDate end = java.time.LocalDate.parse(endDate);
            
            switch (period) {
                case "monthly":
                    java.time.LocalDate current = start.withDayOfMonth(1);
                    while (!current.isAfter(end)) {
                        periods.add(current.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM")));
                        current = current.plusMonths(1);
                    }
                    break;
                case "quarterly":
                    current = start.withDayOfMonth(1);
                    while (!current.isAfter(end)) {
                        int quarter = (current.getMonthValue() - 1) / 3 + 1;
                        periods.add(current.getYear() + "-Q" + quarter);
                        current = current.plusMonths(3);
                    }
                    break;
                case "yearly":
                    int startYear = start.getYear();
                    int endYear = end.getYear();
                    for (int year = startYear; year <= endYear; year++) {
                        periods.add(String.valueOf(year));
                    }
                    break;
                case "half-decades":
                    startYear = (start.getYear() / 5) * 5;
                    endYear = (end.getYear() / 5) * 5;
                    for (int year = startYear; year <= endYear; year += 5) {
                        periods.add(year + "-" + (year + 4));
                    }
                    break;
                case "decades":
                    startYear = (start.getYear() / 10) * 10;
                    endYear = (end.getYear() / 10) * 10;
                    for (int year = startYear; year <= endYear; year += 10) {
                        periods.add(year + "s");
                    }
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error generating periods: " + e.getMessage());
            e.printStackTrace();
        }
        return periods;
    }
    
    private String getPeriodSelectClause(String period) {
        switch (period) {
            case "monthly":
                return "strftime('%Y-%m', cd.DMY)";
            case "quarterly":
                return "strftime('%Y', cd.DMY) || '-Q' || (CASE WHEN strftime('%m', cd.DMY) IN ('01','02','03') THEN '1' " +
                       "WHEN strftime('%m', cd.DMY) IN ('04','05','06') THEN '2' " +
                       "WHEN strftime('%m', cd.DMY) IN ('07','08','09') THEN '3' " +
                       "ELSE '4' END)";
            case "yearly":
                return "strftime('%Y', cd.DMY)";
            case "half-decades":
                return "CAST((strftime('%Y', cd.DMY) / 5) * 5 AS TEXT) || '-' || CAST((strftime('%Y', cd.DMY) / 5) * 5 + 4 AS TEXT)";
            case "decades":
                return "CAST((strftime('%Y', cd.DMY) / 10) * 10 AS TEXT) || 's'";
            default:
                return "strftime('%Y', cd.DMY)";
        }
    }
    
    private static class StationData {
        String stationId;
        String stationName;
        java.util.Map<String, Double> periodAverages = new java.util.HashMap<>();
        double difference;
    }
}
