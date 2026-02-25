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
public class PageEquip implements Handler {

    public static final String URL = "/equipment.html";

    @Override
    public void handle(Context context) throws Exception {
        String html = "<html>";

        html += "<head><title>Climate Equipment</title>";
        html += "<link rel='stylesheet' type='text/css' href='common.css' />";
        html += "</head><body>";

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

        html += "<div class='header'><h1>Summary of Dataset</h1></div>";
        html += "<div class='content'>";

        html += """
            <div class="input-border">
                <form action="/equipment.html" method="post" class="top-bar">
                    <div class="dropdown-option">
                        <label for="datasettype1_drop" class="dropdown-label" >Data Set Type:</label>
                        <select id="datasettype1_drop" name="datasettype1_drop" class="dropdown-select">
                        <option value="" selected disabled hidden>Choose Field</option>
        """;
        //loop through field for dropdown options
        JDBCConnection jdbc = new JDBCConnection();
        ArrayList<FieldDescription> fieldDesc = jdbc.getFieldDescription(); //gets the jdbc connection methods 
        ArrayList<String> allfields = new ArrayList<>();
        for (FieldDescription fd : fieldDesc) {
            allfields.add(fd.getField()); //takes the field attribute initiallky put into getfieldiscription method
        };
        for(int i=0; i<allfields.size(); ++i){
            html += "<option value='" + allfields.get(i) +"'>" + allfields.get(i) + "</option>"; //outputs options in loop 
        };
                                  
        html += " </select>";
        //reset button
        html += """
                
                
        <div class='apply-button'>
        <button type='submit' class='btn btn-primary reset-btn 'name='datasettype1_drop' value=''>Reset</button>
        </div>
        """;
                
                


        html += """
                </div>
                <div class="apply-button">
                        <button type="submit" class="btn btn-primary custom-btn">Apply</button>
                </div>
                </form>
        """;

        // Determine status message
        String statusMessage;
        String selected = context.formParam("datasettype1_drop"); //stores the field selected in dropdown box
        if (selected == null) {
            statusMessage = "<div class='status-box color'>You must select a dataset option.</div>";
        } 
        else if (selected.equals("")) {
        statusMessage = "<div class='status-box success'>Reset has been applied.</div>";
        }   else {
            statusMessage = "<div class='status-box success'>Option '" + selected + "' has been applied.</div>";
        }

        //adds the information icon next to instruction 
        html += "<link rel='stylesheet' href='https://fonts.googleapis.com/icon?family=Material+Icons'>";
        html += "<div class='bottom-row'>";
        html += "<div class='instruction-container tooltip'>";
        html += "<i class='material-icons p1infoicon'>info</i>"; //creates that info icon
        html += "<span class='instruction-text'>Instructions</span>";
        html += "<span class='tooltiptext'>";
        html += "Webpage with an informative summary of each field/attribute that powers our website. The table below lists all of the fields/attributes and their descriptions. To get information on a specific field/attribute quickly, click the dropdown box, select a field, and press the apply button. The status box indicates whether the request was successfully applied.";
        html += "</span>";
        html += "</div>";
        html += statusMessage; //to output status box messages/
        html += "</div>"; // closes bottom-row
        html += "</div>"; // closes input-border

        
        html += outputFieldDescription(selected);

        html += "</div>"; // closes content

        html += "</body></html>";

        context.html(html);
    }

    //method for outputting the data 
    public String outputFieldDescription(String selectedField) {
        
        String html = "";
        JDBCConnection jdbc = new JDBCConnection();
        ArrayList<FieldDescription> fieldDesc = jdbc.getFieldDescription();
        html = html + "<div class='data-border1' >";
        html += "<table style='width: 100%; border-collapse: collapse; margin-top: 20px;'>"; 
        html += "<colgroup>";
        html +=   "<col style='width: 30%;'>";
        html +=   "<col style='width: 70%;'>";
        html += "</colgroup>";
        html += "<tr><th>Field</th><th>Description</th></tr>";

        for (FieldDescription fd : fieldDesc) {
            if (selectedField == null || selectedField.equals("") || selectedField.equals(fd.getField())){
                html += "<tr>";
                html += "<td>" + fd.getField() + "</td>";
                html += "<td>" + fd.getDescription() + "</td>";
                html += "</tr>";
            }
        }

        html += "</table>";
        html = html + "</div>";

        return html;
    }
}


