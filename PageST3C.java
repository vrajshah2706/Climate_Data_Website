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
import java.time.Year;

/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin
 * by writing the raw HTML into a Java String object
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class PageST3C implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page3C.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html = html + "<head>" + 
               "<title>Subtask 3.3</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        html = html + "<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css'>";
        html = html + "</head>";

        // Add the body
        html = html + "<body>";

        // Add the topnav
        // Add the topnav
        
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
                <h1>Impact of Metrics</h1>
            </div>
        """;
        //adds the information icon next to instruction 
        html += "<link rel='stylesheet' href='https://fonts.googleapis.com/icon?family=Material+Icons'>";
        //for status box message : 
        
        String statusMessage = "<span class='status-message'>No option selected.</span>";

        if (context.formParam("weatherStation_drop") == null || context.formParam("weatherStation_drop").isEmpty()) {
    statusMessage = "<span class='status-message error'>You must select a weather station, Reference metric, start and end year.</span>";
} else if (context.formParam("startDate_drop") == null || context.formParam("startDate_drop").isEmpty()) {
    statusMessage = "<span class='status-message error'>You must select a Start Date.</span>";
} else if (context.formParam("endDate_drop") == null || context.formParam("endDate_drop").isEmpty()) {
    statusMessage = "<span class='status-message error'>You must select an End Date.</span>";
} else if (context.formParam("referenceMetric_drop") == null || context.formParam("referenceMetric_drop").isEmpty()) {
    statusMessage = "<span class='status-message error'>You must select a Reference Climate Metric.</span>";
} else {
    // All required fields are filled
    String startDate = context.formParam("startDate_drop");
    String endDate = context.formParam("endDate_drop");
    String weatherStation = context.formParam("weatherStation_drop");
    String referenceMetric = context.formParam("referenceMetric_drop");

    // Validate date range
    if (startDate.compareTo(endDate) > 0) {
        statusMessage = "<span class='status-message error'>Start Year cannot be Higher than end Year.</span>";
    } else {
        statusMessage = "<span class='status-message success'>Weather station <b>" + weatherStation + "</b> and reference climate metric <b>" + referenceMetric +
            "</b> for start date <b>" + startDate + "</b> and end date <b>" + endDate + "</b> has been applied.</span>";
    }
}
    
        // Add Div for page Content
        html = html + "<div class='content'>";
        
        // Add HTML for the page content
         html = html + """
                <div class="input-borderfor3C">
                    <form action="/page3C.html" method="post" class="top-bar">

                      
                        <div class="form-row">
                            <div class="dropdown-label1 moveChoose">Choose:</div>
                            <div class ="dropdown-label1 moveTimeperiod">Time period:</div>
                            
                            <div class="moveInstruction3C">
                            <div class='instruction-container tooltip instruction-bottom'>
                            <i class='material-icons p1infoicon'>info</i>
                            <span class='instruction-text'>Instructions</span>
                            <span class='tooltiptext1'>
                            Webpage for correlation of Metrics
                            1) Select Station ID, Start & End Year, Reference Metric 
                            2) click "Find Correlation"
                            3) 
                                a)If you see zeros that means there is no data for that selection
                                b)If one of the coloumns is 0 that will give percent change as 0 as 
                                  accurate calculations cant be made for the selected start & end Year.
                                c)the calculations are based on Halfdecade, the format is this E.g 1970
                                  to 1974 = 1970 (halfdecade), 1975-1979 = 1975 (halfdecade).
                            </span>
                            </div>
                            </div>
                            
                        </div>

                       
                        <div class="form-row">
  <div class="moveWeatherstation">
    <label class="dropdown-label1" for="weatherStation_drop" >Weather Station:</label>
    <select class="dropdown-select1" id="weatherStation_drop" name="weatherStation_drop">
      
      <option value="">-- Select--</option>
                    """;
                    //output station ids 
                    JDBCConnection jdbc = new JDBCConnection();
                    ArrayList<String> ids = jdbc.get3CStationIds();

                        for (int i = 0; i < ids.size(); ++i) {
                       
                        String id1 = ids.get(i);
                        
                            html += "<option value='" + id1 + "'>" + id1 + "</option>"; // Show label in dropdown
                        }
                    
                            
                    html += "</select>";
                    html = html + """
    
  </div>

  <div class="movestartYear">
    <label class="dropdown-label1" for="startDate_drop">Start Year:</label>
    
    """;
    html += "<select id='startDate_drop' name='startDate_drop' class='dropdown-select1'>";
    html += "<option value=''>-- Select Year --</option>";
    for (int year = 1970; year <= 2020; year++) {
        html += "<option value='" + year + "'>" + year + "</option>";
    }
    html += "</select>";
            
           
    html = html + """
  </div>

  <div class="status-box2">
  """;
    html += statusMessage;   
          
html += """
        
        
  </div>
</div>


                        
     <div class="form-row">
  
  <div>
    <label class="dropdown-label1" for="referenceMetric_drop">Reference Climate Metric:</label>
    <select class="dropdown-select1" id="referenceMetric_drop" name="referenceMetric_drop">

    <option value="">-- Select--</option>
                    """;
                    //output mertics ids 
                    
                    ArrayList<String> ccvmetric = jdbc.getCCVMetrics();

                        for (int i = 0; i < ccvmetric.size(); ++i) {
                       
                        String metric1 = ccvmetric.get(i);
                        
                            html += "<option value='" + metric1 + "'>" + metric1 + "</option>"; // Show label in dropdown
                        }
                    
                            
                    html += "</select>";
                    html = html + """
    
      




    </select>
  </div>

  
  <div>
    <label class="dropdown-label1" for="endDate_drop">End Year:</label>
    
    """;
    html += "<select id='endDate_drop' name='endDate_drop' class='dropdown-select1'>";
    html += "<option value=''>-- Select Year --</option>";
    for (int year = 1970; year <= 2020; year++) {
        html += "<option value='" + year + "'>" + year + "</option>";
    }
    html += "</select>";
            
           
    html = html + """
  </div>

  
  <div class="apply-button">
    <button type="submit" name="mainApply" class="btn btn-primary custom-btn1">Find Correlation</button>
  </div>
</div>


                    </form>
                </div>
                """;
    if (context.formParam("startDate_drop") != null && !context.formParam("startDate_drop").isEmpty() &&
    context.formParam("endDate_drop") != null && !context.formParam("endDate_drop").isEmpty() &&
    context.formParam("weatherStation_drop") != null && !context.formParam("weatherStation_drop").isEmpty() &&
    context.formParam("referenceMetric_drop") != null && !context.formParam("referenceMetric_drop").isEmpty()) {

    int startYear = Integer.parseInt(context.formParam("startDate_drop"));
int endYear = Integer.parseInt(context.formParam("endDate_drop"));
int stationId = Integer.parseInt(context.formParam("weatherStation_drop"));
String referenceMetric = context.formParam("referenceMetric_drop");

ArrayList<ArrayList<String>> dataRows = jdbc.get3CAnalysisData(stationId, startYear, endYear, referenceMetric);




ArrayList<Integer> halfDecades = new ArrayList<>();

int actualStart = (startYear / 5) * 5;
int actualEnd = ((endYear + 4) / 5) * 5;

if (endYear - startYear < 5) {
    // One half-decade only
    halfDecades.add(actualStart);
} else {
    for (int year = actualStart; year <= actualEnd; year += 5) {
        halfDecades.add(year);
    }
}


//to avoid overflow of table after selecting 1970-2020
int maxCols = 7; // Maximum number of halfDecade columns per table
int totalCols = halfDecades.size();
int numTables = (int) Math.ceil((double) totalCols / maxCols);

for (int t = 0; t < numTables; t++) {
    int start = t * maxCols;
    int end = Math.min(start + maxCols, totalCols);
    List<Integer> currentCols = halfDecades.subList(start, end);

    boolean isLastTable = (t == numTables - 1); 

    html += "<div class='data-border1'>";
    html += "<table style='width: 100%; border-collapse: collapse; margin-top: 20px;'>";

    // Header row
    html += "<tr><th>Metric</th>";
    for (int hd : currentCols) {
        html += "<th>" + hd + "</th>";
    }
    if (isLastTable) {
        html += "<th>% Change</th><th>Trend</th>";
    }
    html += "</tr>";

    // Data rows
    for (ArrayList<String> row : dataRows) {
        html += "<tr>";
        html += "<td>" + row.get(0) + "</td>"; // Metric name

        for (int i = start + 1; i < end + 1; i++) {
            String cell = (i < row.size()) ? row.get(i) : null;
            html += "<td>" + (cell == null ? "0" : cell) + "</td>";
        }

        if (isLastTable) {
            
            String trend = row.get(row.size() - 2);          // "Trend"
            String percentChange = row.get(row.size() - 1);  // "% Change"
            html += "<td>" + (percentChange == null ? "0" : percentChange) + "</td>"; // % Change
            html += "<td>" + (trend == null ? "0" : trend) + "</td>"; // Trend
        }

        html += "</tr>";
    }

    // No data case
    if (dataRows.isEmpty()) {
        int colspan = currentCols.size() + (isLastTable ? 3 : 1);
        html += "<tr><td colspan='" + colspan + "'>No data found for this selection.</td></tr>";
    }

    html += "</table>";
    html += "</div>";
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
    

    

    



}
