package app;

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
public class PageST2C implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page2C.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html = html + "<head>" + 
               "<title>Subtask 2.3</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        html = html + "<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css'>";
        html = html + "</head>";

        // Add the body
        html = html + "<body>";

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
                <h1>Data Quality</h1>
            </div>
        """;
        //status info 
         String statusMessage = "<div class='status-message'>No option selected.</div>";

        if (context.formParam("qualityflag_drop") == null || context.formParam("qualityflag_drop").isEmpty()) {
                 statusMessage = "<div class='status-message error'>You must select a quality flag option.</div>";
        } 
        else if (context.formParam("startDate_drop") == null || context.formParam("startDate_drop").isEmpty()) {
                statusMessage = "<div class='status-message error'>You must select a start date.</div>";
        }       
        else if (context.formParam("endDate_drop") == null || context.formParam("endDate_drop").isEmpty()) {
                statusMessage = "<div class='status-message error'>You must select an end date.</div>";
        } 
        else if (context.formParam("qualitymetric_drop") == null || context.formParam("qualitymetric_drop").isEmpty()) {
                statusMessage = "<div class='status-message error'>You must select a quality metric option.</div>";
        } 
        else {
            String startDate = context.formParam("startDate_drop");
            String endDate = context.formParam("endDate_drop");

    // Validate date range
    if (startDate.compareTo(endDate) > 0) {
        statusMessage = "<div class='status-message error'>Start date cannot be Higher than end date.</div>";
    } else {
        String qualityFlag = context.formParam("qualityflag_drop");
        String qualityMetric = context.formParam("qualitymetric_drop");

        statusMessage = "<div class='status-message success'>Quality flag <b>" + qualityFlag + "</b> and quality metric <b>" + qualityMetric +
                "</b> for start date <b>" + startDate + "</b> and end date <b>" + endDate + "</b> has been applied.</div>";
    }
}

        // Add Div for page Content
        html = html + "<div class='content'>";
        html = html + """
                        <div class="input-border1">
                        <form action="/page2C.html" method="post" class="top-bar">
                        <div class=form-row>
                        <div>
                        <label for="qualityflag_drop" class="dropdown-label1 moveQualityflag">Quality Flag:</label>
                        <select id="qualityflag_drop" name="qualityflag_drop" class="dropdown-select1">
                        <option value="">-- Select--</option>
                        
                    """;
                        //output flags 
                        JDBCConnection jdbc = new JDBCConnection();
                        ArrayList<String> Flags = jdbc.getQualityFlag();

                        for (int i = 0; i < Flags.size(); ++i) {
                        String flag = Flags.get(i);
                        html += "<option value='" + flag + "'>" + flag + "</option>"; // Show label in dropdown
                        }


                        html += "</select>";
                        html = html+ """
                            
                            
                        </div>

                        <div>
                        <label for="startDate_drop" class="dropdown-label1 moveStartDate" >Start Date:</label>
                        <input type="date" id="startDate_drop" name="startDate_drop" min='1970-01-01' max='2020-12-31' >
                        </div>

                        <div>
                        <label for="sortby_drop" class="dropdown-label1 " >Sort By:</label>
                        <select id="sortby_drop" name="sortby_drop" class="dropdown-select1" ">
                        <option>Ascending</option>
                        <option>Descending</option>
                        </select>
                        </div>
                        
                    </div>
                    <div class=form-row>
                    <div>
                        <label for="qualitymetric_drop" class="dropdown-label1 movequalitymetric">Quality Metric:</label>
                        <select id="qualitymetric_drop" name="qualitymetric_drop" class="dropdown-select1">
                        <option value="">-- Select--</option>
                    """;
                    //output climate metric 
                    
                    ArrayList<String> metrics = jdbc.getClimateMetrics2();

                        for (int i = 0; i < metrics.size(); ++i) {
                       
                        String metric1 = metrics.get(i);
                        if (!(metric1.endsWith("Qual") ||metric1.endsWith("QUal"))) {
                            html += "<option value='" + metric1 + "'>" + metric1 + "</option>"; // Show label in dropdown
                        }
                    }
                            
                    html += "</select>";
                    html = html + """
                    </div>      
                            
                    
                    <div>
                        <label for="endDate_drop" class="dropdown-label1 moveEndDate">End Date:</label>
                        <input type="date" id="endDate_drop" name="endDate_drop" min='1970-01-01' max='2020-12-31' >
                    </div>
                    
                    

  
                    <div class= moveInstruction>
                    <div class='instruction-container tooltip instruction-bottom'>
                        <i class='material-icons p1infoicon'>info</i>
                        <span class='instruction-text'>Instructions</span>
                        <span class='tooltiptext1'>
                        webpage that uses qualityflag and qualitymetric selections to view data quality across all states and regions. 
                        For summary table: 
                        1)Choose Quality Metric,Quality Flag, Start & End date
                        2)Click "Apply" to see table 
                        for generate summary table (exlcudes quality flag 'Y'):
                        1)Choose state, Month & Year, Climate metric
                        2) Click "Apply"
                        The status box helps you determine whether the selections were applied. Despite being successful, if you don't see anything, it indicates that there is no data available for that selection.
                        </span>
                    </div>

                    </div>
                    </div>
                    
                   
  <div class=form-row>
  <div class='apply-button'>
    <button type='submit' name='mainApply' class='btn btn-primary custom-btn1'>Apply</button>
  </div>


        
  
         </form>
      """;
              
             
    
    html = html + """
            
            
 <form action="/page2C.html" method="post" class="top-bar">
  <div class="popup-container">
    <span class="hover-trigger">Generate Summary</span>
    <div class="popup-content">
 <h4>Summary On</h4>

  <label for="state">State:</label>
  <select id="state" name="state" required>
    <option value="">--Select--</option>
    <option value="VIC">Victoria</option>
    <option value="A.A.T.">Australian Antarctic Territory</option>
    <option value="A.E.T">Australian Capital Territory</option>
    <option value="N.S.W.">New South Whales</option>
    <option value="N.T.">Northern Territory</option>
    <option value="QLD">Queensland</option>
    <option value="TAS">Tasmania</option>
    <option value="S.A.">South Australia</option>
    <option value="W.A.">Western Australia</option>
  </select>

  <label for="monthSelect">Select Month and Year:</label>
  <div style="display: flex; gap: 10px; margin-bottom: 10px;">
    <select id="monthSelect" name="monthSelect" required>
      <option value="">Month</option>
      <option value="01">January</option>
      <option value="02">February</option>
      <option value="03">March</option>
      <option value="04">April</option>
      <option value="05">May</option>
      <option value="06">June</option>
      <option value="07">July</option>
      <option value="08">August</option>
      <option value="09">September</option>
      <option value="10">October</option>
      <option value="11">November</option>
      <option value="12">December</option>
      
    </select>

    <select id="yearSelect" name="yearSelect" required>
      <option value="">Year</option>
      """;
              
              
      for (int year = 1970; year <= 2020; year++) {
        html += "<option value='" + year + "'>" + year + "</option>";
    }
    html = html + """
            
            
    </select>
  </div>


          
          

  

        
      

     
              
              
      <label for="metric">Climate Metric:</label>
      <select id="metric" name="metric">
      """;
              
      

            for (int i = 0; i < metrics.size(); ++i) {
                       
                        String metric1 = metrics.get(i);
                        if ((metric1.endsWith("Qual") ||metric1.endsWith("QUal"))) {
                            html += "<option value='" + metric1 + "'>" + metric1 + "</option>"; // Show label in dropdown
                        }
                    }            
                            
    html += "</select>";
    html += """
            

     
  <div class="apply-button">
    <button type="submit" name="summaryApply" class="btn btn-primary custom-btn1">Apply</button>
  </div>
    </div>
  </div>
</div>

   
            
            


        
        
                    

       
        
                    </form>
                    

                """; 
        
                    
        
        
        
          

        
   

       

        //adds the information icon next to instruction 
        html += "<link rel='stylesheet' href='https://fonts.googleapis.com/icon?family=Material+Icons'>";
        
        String MetricSelected = context.formParam("qualitymetric_drop");
        String FlagSelected = context.formParam("qualityflag_drop");

        // Convert start and end dates
        String userInputSD = context.formParam("startDate_drop");
        String userInputED = context.formParam("endDate_drop");

        

        String twoCStartDate = userInputSD;
        String twoCEndDate = userInputED;
        String sort = context.formParam("sortby_drop");
        

        
        //Get main table data

        ArrayList<String> mainTable = jdbc.get2CMainTable(FlagSelected, MetricSelected, twoCStartDate, twoCEndDate, sort);


        html += statusMessage; //to output status box messages/
        html += "</div>"; // closes bottom-row
        html = html + "</div>"; //close input border

        
        

        //summary table
        String state = context.formParam("state");
        // String yearMonth = context.formParam("monthPicker");
        String Summarymetric =  context.formParam("metric");
        // New: Reconstruct monthPicker manually
        String selectedMonth = context.formParam("monthSelect");
        String selectedYear = context.formParam("yearSelect");
        String yearMonth = null;
        if (selectedMonth != null && selectedYear != null && !selectedMonth.isEmpty() && !selectedYear.isEmpty()) {
            yearMonth = selectedYear + "-" + selectedMonth;
        }

        System.out.println("DEBUG -- Summary Inputs:");
        System.out.println("State: " + state);
        System.out.println("Month/Year: " + yearMonth);
        System.out.println("Metric Column: " + Summarymetric);

        ArrayList<String> summary = jdbc.get2CSummaryData(state, yearMonth, Summarymetric);
        

        if (context.formParam("mainApply") != null) {
    // show main table
     html += outputMainTable(mainTable, MetricSelected);
} else if (context.formParam("summaryApply") != null) {
    // show summary table
    html += outputSummaryTable(summary,Summarymetric );
}
        // Close Content div
        html = html + "</div>";

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";
        

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

    public String outputMainTable(ArrayList<String> mainTable, String metricSelected) {
    String html = "";
    html += "<div class='data-border1'>";
    html += "<table style='width: 100%; border-collapse: collapse; margin-top: 20px;'>";
    html += "<tr><th>Station</th><th>Date</th><th>" + metricSelected + "</th><th>" + metricSelected + "Qual</th></tr>";

    // Print each row: every 4 entries = 1 row
    for (int i = 0; i < mainTable.size(); i += 4) {
        html += "<tr>";
        html += "<td>" + mainTable.get(i) + "</td>";         // StationID
        html += "<td>" + mainTable.get(i + 1) + "</td>";     // DMY
        html += "<td>" + mainTable.get(i + 2) + "</td>";     // Metric
        html += "<td>" + mainTable.get(i + 3) + "</td>";     // Qual
        html += "</tr>";
    }
    if (mainTable.isEmpty()) {
    html += "<tr><td colspan='4'>No data found for this selection.</td></tr>";
}

    html += "</table>";
    html += "</div>";

    return html;
}

public String outputSummaryTable(ArrayList<String> summary, String Summarymetric ) {
    String html = "";
    html += "<div class='data-border1'>";
    html += "<table style='width: 100%; border-collapse: collapse; margin-top: 20px;'>";
    html += "<tr><th>Flags</th><th>" + Summarymetric + " (count)</th></tr>";
        
    for(int i=0; i<summary.size(); i+=2){
        html += "<tr>";
        html += "<td>" + summary.get(i) + "</td>";         // flags
        html += "<td>" + summary.get(i + 1) + "</td>";     // flags count
        html += "</tr>";
    }
    if (summary.isEmpty()) {
    html += "<tr><td colspan='2'>No data found for this selection.</td></tr>";
}
    html += "</table>";
    html += "</div>";
    return html;

}
        
    
}
