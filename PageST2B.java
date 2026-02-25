package app;

import java.util.List;
import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin
 * by writing the raw HTML into a Java String object
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class PageST2B implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page2B.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html = html + "<head>" + 
               "<title>Subtask 2.2</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
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
                <h1>Subtask 2.B</h1>
            </div>
        """;

        // Add Div for page Content
        html = html + "<div class='content'>";

        // Add HTML for the page content
        html = html + """
            
            <h2>Check Weather Trends</h2>

            <div class='input-border'>

            <span class='hoverInstructions'>Instructions: Hover to see<br></span>
            <div class='InstructionsForUser'>
            <h5>How to Get Daily Values of a Metric<h5>
            <ol>
                <li><strong>Climate Metric: </strong>Select the metric (e.g. Precipitation, Temperature)</li>
                <li><strong>Station Range: </strong>Enter a Start Station ID and End Station ID (e.g. 1006 to 3003)</li>
                <li><strong>Date Range: </strong>Choose dates between 01/01/1970 and 31/12/2020</li>
            </ol>
            <br>
            <h5>How to Get the Summary of Daily Values of a Metric : Optional<h5>
            <ol>
                <li><strong>Filter by Time: </strong>Choose one or more time periods (e.g. Decades + Half Decades)</li>
                <li><strong>Filter by Location: </strong>Broad/Precise Search: By State/ By Station ID</li>
                <li><strong>Summary Type: </strong>Average/ Total/ Minimum/ Maximum</li>
                <li><strong>Order Results By: </strong>Time or Location : Orders the column by chosen option</li>
            </ol>
            <br>
            <p>Click \"Apply\" to generate results</p>
            </div>  

            <form action='/page2B.html' method='post'>

            <div class='form-group'>

            <div>
                <label for='climateMetric_drop'><b>Climate Metric</b></label>
                <select class='dropDown' id='climateMetric_drop' name='climateMetric_drop' list='metrics' required>
                <datalist id='metrics'>
                """;
        
        JDBCConnection jdbc = new JDBCConnection();
        List<String> climateMetrics = jdbc.getClimateMetrics();
        for (String climateMetric : climateMetrics) {
            html = html + "<option>" + climateMetric + "</option>";
        }

        html = html + """
                </select>
                </datalist>

                <span>
                <label for='fromStationID_drop'><b>From Station ID</b></label>
                <select class='dropDown' id='fromStationID_drop' name='fromStationID_drop' list='startStationIDs' title='Choose the first station ID' required>
                <datalist id='startStationIDs'>
                    """;

        ArrayList<String> stationIds = jdbc.getStationIds();
        for (String stationId : stationIds) {
            html = html + "<option>" + stationId + "</option>";
        }
        html = html + """
                
                </datalist>
                </select>
                </span>

                <span>
                <label for='toStationID_drop'><b>To Station ID</b></label>
                <select class='dropDown' id='toStationID_drop' name='toStationID_drop' list='endStationIDs' title='Choose the last station ID' required>
                <datalist id='endStationIDs'>    
                        """;

        for (String stationId : stationIds) {
            html = html + "<option>" + stationId + "</option>";
        }
        
        html = html + """
                </datalist>
                </select>
                </span>

                <span>
                <label for='startDate_drop'><b>Start Date</b></label>
                <input class='dropDown' type='date' id='startDate_drop' name='startDate_drop' min='1970-01-01' max='2020-12-31' value='1970-01-01' title='Choose a start date' required>
                </span>

                """;

        html = html + """

                <span>
                <label for='endDate_drop'><b>End Date</b></label>
                <input class='dropDown' type='date' id='endDate_drop' name='endDate_drop' min='1970-01-01' max='2020-12-31' value='1970-01-05' title='Choose an end date' required>
                """;
        
        html = html + """
                </span>
                </div>
                <br>

                <details>
                    <summary>Summary Filters</summary><hr>

                    <div>

                    <b>Filter by Time</b><br>
                    <div class='checkboxes'>

                    <input type='checkbox' id='filterByTime_checkbox1' name='timeFilter' value='Decade'>
                    <label for='filterByTime_checkbox1'>Decades</label><br>

                    <input type='checkbox' id='filterByTime_checkbox2' name='timeFilter' value='HalfDecade'>
                    <label for='filterByTime_checkbox2'>Half Decades</label><br>
 
                    <input type='checkbox' id='filterByTime_checkbox3' name='timeFilter' value='Year'>
                    <label for='filterByTime_checkbox3'>Years</label><br>

                    <input type='checkbox' id='filterByTime_checkbox4' name='timeFilter' value='Half'>
                    <label for='filterByTime_checkbox4'>Half Years</label><br>

                    <input type='checkbox' id='filterByTime_checkbox5' name='timeFilter' value='Quarter'>
                    <label for='filterByTime_checkbox5'>Quarter Years</label><br>

                    <input type='checkbox' id='filterByTime_checkbox6' name='timeFilter' value='Month'>
                    <label for='filterByTime_checkbox6'>Months</label><br>

                    <input type='checkbox' id='filterByTime_checkbox7' name='timeFilter' value='Week'>
                    <label for='filterByTime_checkbox7'>Weeks</label><br>

                    <input type='checkbox' id='filterByTime_checkbox8' name='timeFilter' value='Date'>
                    <label for='filterByTime_checkbox8'>Days</label><br><hr>

                    </div>

                    <b>Filter by Location</b><br>
                    <div class='checkboxes'>

                    <input type='checkbox' id='filterByLocation_checkbox1' name='locationFilter' value='state'>
                    <label for='filterByLocation_checkbox1'>State</label><br>

                    <input type='checkbox' id='filterByLocation_checkbox2' name='locationFilter' value='site'>
                    <label for='filterByLocation_checkbox2'>Station ID</label><br><hr>

                    </div>

                    <div>
                    <label for='summaryType_drop'><b>Summary Type</b></label>
                    <select class='dropDown' id='summaryType_drop' name='summaryType_drop' required>
                        <option value='avg'>Average</option>
                        <option value='sum'>Total</option>
                        <option value='max'>Maximum</option>
                        <option value='min'>Minimum</option>
                    </select>
                    </div><hr>

                    <div>
                    <label for='orderBy_drop'><b>Order by</b></label>
                    <select class='dropDown' id='orderBy_drop' name='orderBy_drop' required>
                        <option value='Time'>Time</option>
                        <option value='Location'>Location</option>
                    </select>
                    </div>

                    </div>    

                </details>

            </div>
            
                <div>
                <button class='applyButton' type='submit'>Apply</button>
                <button class='resetButton' type='reset'>Reset</button>
                </div>
                
            </div>
            </form>
            </div> 
            
        """;

        String climateMetric = context.formParam("climateMetric_drop");
        String fromStationID = context.formParam("fromStationID_drop");
        String toStationID = context.formParam("toStationID_drop");
        String startDate = context.formParam("startDate_drop");
        String endDate = context.formParam("endDate_drop");
        List<String> timeFilter = context.formParams("timeFilter");
        List<String> locationFilter = context.formParams("locationFilter");
        String summaryType = context.formParam("summaryType_drop");
        String orderBy = context.formParam("orderBy_drop");


        int fromStationINT = 0;
        int toStationINT = 0;

        if (fromStationID != null) {
            for (int i = 0; i < fromStationID.length(); ++i) {
                if (fromStationID.charAt(i) == ' ') {
                    fromStationID = fromStationID.substring(0, i);
                    fromStationINT = Integer.parseInt(fromStationID);
                    break;
                }
            }
        }
        if (toStationID != null) {
            for (int i = 0; i < toStationID.length(); ++i) {
                if (toStationID.charAt(i) == ' ') {
                    toStationID = toStationID.substring(0, i);
                    toStationINT = Integer.parseInt(toStationID);
                    break;
                }
            }
        }

        


        /*if ((climateMetric == null) || (fromStationID == null) || (toStationID == null) || (startDate == null) || (endDate == null)) {
            html = html + "<p>Select all options</p>";
            
        } */
        if (climateMetric == null) {
            html = html + "Select Climate Metric";
        }
        if (fromStationID == null) {
            html = html + "Select Start Station ID";
        }
        if (toStationID == null) {
            html = html + "Select End Station ID";
        }
        if (startDate == null) {
            html = html + "Select Start Date";
        }
        if (endDate == null) {
            html = html + "Select End Date";
        }
        else {

            String summaryTypeForHeading;
            if (summaryType.compareToIgnoreCase("avg") == 0) {
                summaryTypeForHeading = "Average";
            }
            else if (summaryType.compareToIgnoreCase("sum") == 0) {
                summaryTypeForHeading = "Total";
            }
            else if (summaryType.compareToIgnoreCase("min") == 0) {
                summaryTypeForHeading = "Minimum";
            }
            else {
                summaryTypeForHeading = "Maximum";
            }

            //html = html + "<p>" + climateMetric + "<br>" + fromStationID + "<br>" + toStationID + "<br>" + startDate + "<br>" + endDate + "</p><br>";

            ArrayList<String> dailyValues = new ArrayList<String>();
     
            dailyValues = jdbc.getDailyValues(climateMetric, fromStationINT, toStationINT, startDate, endDate);

            //Daily Values Table
            if (!dailyValues.isEmpty()) {
                html = html + "<h3>&nbsp;&nbsp;Daily Values Table</h3>";
                html = html + "<div class='scrollabelContainer'>";
                html = html + "<table class='scrollableTable' id='dailyValuesTable'>";

                html = html + """
                    <tr>
                        <th> Station ID </th>
                        <th> Date </th>
                        <th>
                    """;
    
                html = html + climateMetric + "</th></tr>";
    
                for (int i = 0; i < dailyValues.size(); i += 3) {
                    html = html + "<tr><td>" + dailyValues.get(i) + "</td>";
                    html = html + "<td>" + dailyValues.get(i+1) + "</td>";
                    html = html + "<td>" + dailyValues.get(i+2) + "</td></tr>";
                }
            
                html = html + "</table></div><br>";
            }
            else {
                html = html + "<p>No data found for the selected criteria to output Daily Values Table</p>";
            }

            if (!(timeFilter.isEmpty()) && !(locationFilter.isEmpty()) && (summaryType != null) && (orderBy != null)) {

                int numColumns = timeFilter.size() + locationFilter.size() + 1;
                int timePeriodColumns = timeFilter.size();
                int locationColumns = locationFilter.size();

                ArrayList<String> summaryTableValues = new ArrayList<String>();
                summaryTableValues = jdbc.getSummaryTable(climateMetric, fromStationINT, toStationINT, startDate, endDate, timeFilter, locationFilter, summaryType, orderBy);

                if (orderBy.compareTo("Time") == 0) {    
                    
                    if (!summaryTableValues.isEmpty()) {
                        numColumns = timeFilter.size() + locationFilter.size() + 1;

                        //Summary Table
                        html = html + "<h3>Summary Table</h3>";
                        html = html + "<div class='scrollabelContainer'>";
                        html = html + "<table class='scrollableTable' id='summaryTable2B'>";
                    
                        html = html + "<tr><th colspan='" + timePeriodColumns + "'>Time Period</th>";
                        html = html + "<th colspan='" + locationColumns + "'>Location</th>";
                        html = html + "<th rowspan='2'>" + summaryTypeForHeading + " " + climateMetric + "</th></tr>";

                        html = html + "<tr>";
                        for (String time : timeFilter) {
                            html = html + "<th>" + time + "</th>";
                        }
                        for (String location : locationFilter) {
                            html = html + "<th>" + location + "</th>";
                        }
                        html = html + "</tr>";

                        html = html + "<tr><td>" + summaryTableValues.get(0) + "</td>";
                        for (int i = 1; i < summaryTableValues.size(); ++i) {
                            if ((i % numColumns) == 0) {
                                html = html + "</tr><tr><td>" + summaryTableValues.get(i) + "</td>";
                            }
                            else {
                                html = html + "<td>" + summaryTableValues.get(i) + "</td>";
                            }
                        }
                        
                        html = html + "</table>";
                        html = html + "</div>"; //Close tables content
                    }
                    else {
                        html = html + "<p>No data found for the selected criteria to output Summary Table</p>";
                    }

                }

                else if (orderBy.compareToIgnoreCase("Location") == 0) {

                    if (!summaryTableValues.isEmpty()) {
                        numColumns = timeFilter.size() + locationFilter.size() + 1;

                        //Summary Table
                        html = html + "<h3>Summary Table</h3>";
                        html = html + "<div class='scrollabelContainer'>";
                        html = html + "<table class='scrollableTable' id='summaryTable2B'>";
                        html = html + "<tr><th colspan='" + locationColumns + "'>Location</th>";
                        html = html + "<th colspan='" + timePeriodColumns + "'>Time Period</th>";
                        html = html + "<th rowspan='2'>" + summaryTypeForHeading + " " + climateMetric + "</th></tr>";

                        html = html + "<tr>";
                        for (String location : locationFilter) {
                            html = html + "<td>" + location + "</td>";
                        }
                        for (String time : timeFilter) {
                            html = html + "<td>" + time + "</td>";
                        }
                        html = html + "</tr>";
                    
                        numColumns = timeFilter.size() + locationFilter.size() + 1;

                        html = html + "<tr><td>" + summaryTableValues.get(0) + "</td>";
                        for (int i = 1; i < summaryTableValues.size()-1; ++i) {
                            if ((i % numColumns) == 0) {
                                html = html + "</tr><tr><td>" + summaryTableValues.get(i) + "</td>";
                            }
                            else {
                                html = html + "<td>" + summaryTableValues.get(i) + "</td>";
                            }
                        }
                        html = html + "<td>" + summaryTableValues.get(summaryTableValues.size()-1) + "</td></tr>";

                        html = html + "</table>";
                        html = html + "</div>"; //Close tables content
                    }

                    else {
                        html = html + "<p>No data found for the selected criteria to output Summary Table</p>";
                    }
                }
                
                

            }
        }


        /* 
        else if (fromStationID_drop.compareTo(toStationID_drop)) {
            html = html + "<>";
        }
        */

        





        // Close Content div
        html = html + "</div>";

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";
        

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);


  }

}
