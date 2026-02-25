package app;

import java.util.ArrayList;
import java.util.List;

import io.javalin.http.Context;
import io.javalin.http.Handler;

/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin
 * by writing the raw HTML into a Java String object
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class PageST3B implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page3B.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html = html + "<head>" + 
               "<title>Subtask 3.2</title>";

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
                <h1>Subtask 3.B</h1>
            </div>
        """;

        // Add Div for page Content
        html = html + "<div class='content'>";

        // Add HTML for the page content
        html = html + """
            <h2>Similarities Between Different Weather Factors</h2>

            <div class='input-border'>

            <span class='hoverInstructions'>Instructions: Hover to see</span>
            <div class='InstructionsForUser'>
                <h3>How to Compare Metrics</h3>
                <ol>
                    <li><strong>Reference Metric: </strong>Select the reference metric (e.g. Precipitation)</li>
                    <li><strong>Metrics to Compare: </strong>
                    <ul>
                        <li>Enter a number (e.g. 5) to find most similar metrics</li>
                        <li>Choose specific metrics (e.g. Sunshine, Evaporation)</li>
                    </ul>
                    </li>
                    <li><strong>Data Type: </strong>Choose Average, Total,  Minimum or Maximum</li>
                    <li><strong>Data Range: </strong>Choose dates between 01/01/1970 and 31/12/2020</li>
                    <li><strong>Group By: </strong>Choose one or more time periods (e.g. Decades + Half Decades)</li>
                </ol>
                <p>Click \"Apply\" to generate comparison tables</p>
                </div>

            <form form action='/page3B.html' method='post'>
            <div class='form-group'>

                <label for='referenceClimateMetric_drop'>Reference Climate Metric</label>
                <input class='dropDown' id='referenceClimateMetric_drop' name='referenceClimateMetric_drop' list='referenceMetrics' required>
                <datalist id='referenceMetrics'>

                """;
        
        JDBCConnection jdbc = new JDBCConnection();
        List<String> referenceClimateMetrics = jdbc.getClimateMetrics();
        for (int i = 0; i < referenceClimateMetrics.size(); ++i) {
            html = html + "<option>" + referenceClimateMetrics.get(i) + "</option>";  
        }

        html = html + """
                </datalist>
                
                <label for='dataType_drop'>Data Type</label>
                <select  class='dropDown' id='dataType_drop' name='dataType_drop'>
                    <option value='avg'>Average</option>
                    <option value='sum'>Total</option>
                    <option value='max'>Maximum</option>
                    <option value='min'>Minimum</option>
                </select>

                <label for='numberOfMetrics_drop'>Number of Metrics to Compare (1-20):</label>
                <input id='numberOfMetrics_drop' name='numberOfMetrics_drop' type='number' min='1' max='20'>


                <fieldset>
                <legend>Choose metrics to compare</legend> 
                <label><input type='checkbox' name='otherMetrics' value='SelectAll'>Select All</label><br> 
                """;

        List<String> otherClimateMetrics = jdbc.getClimateMetrics();
        for (int i = 0; i < otherClimateMetrics.size(); ++i) {
            html = html + "<label><input type='checkbox' name='otherMetrics' value='" + otherClimateMetrics.get(i) + "'>" + otherClimateMetrics.get(i) + "</label><br>";
            if (((i % 5) == 0) && (i != 0)) {
                html = html + "<br>";
            }
        }
        
        html = html + """
                </fieldset>

                <label for='startDate_drop'>Start Date</label><br>
                <input type='date' id='startDate_drop' name='startDate_drop' title='Choose a start date' min='1970-01-01' max='2020-12-31' value='1970-01-01' required><br>

                <label for='endDate_drop'>End Date</label><br>
                <input type='date' id='endDate' name='endDate' title='Choose an end date' min='1970-01-01' max='2020-12-31' value='1970-02-01' required><br>

                <fieldset>
                <legend>Choose how to group by</legend>
                <label><input type='checkbox' name='groupBy' value='Decade'>Decades</label><br>
                <label><input type='checkbox' name='groupBy' value='HalfDecade'>Half decades</label><br>
                <label><input type='checkbox' name='groupBy' value='Year'>Years</label><br>
                <label><input type='checkbox' name='groupBy' value='Half'>Half years</label><br>
                <label><input type='checkbox' name='groupBy' value='Quarter'>Quarter years</label><br>
                <label><input type='checkbox' name='groupBy' value='Month'>Months</label><br>
                <label><input type='checkbox' name='groupBy' value='Week'>Weeks</label><br>
                <label><input type='checkbox' name='groupBy' value='Date'>Days</label><br>

                </fieldset>

                <button class='applyButton' id='applyButton' type='submit' class='btn btn-primary'>Apply</button>

            </form>
            </div>
            
            
            """;

            String referenceClimateMetric = context.formParam("referenceClimateMetric_drop");
            String dataType = context.formParam("dataType_drop");
            String numberOfMetrics = context.formParam("numberOfMetrics_drop");
            List<String> otherMetrics = context.formParams("otherMetrics");
            String startDate = context.formParam("startDate_drop");
            String endDate = context.formParam("endDate");
            List<String> groupBy = context.formParams("groupBy");

            int numMetrics = 0;

            if ((referenceClimateMetric == null) || (dataType == null) || ((numberOfMetrics == null) && (otherMetrics.isEmpty())) || (startDate == null) || (endDate == null) || (groupBy.isEmpty())) {
                html = html + "<p>Select all options</p>";
            }
            else {
                
                //html = html + "<p>" + referenceClimateMetric + "<br>" + dataType + "<br>" + numberOfMetrics + "<br>" + otherMetrics + "<br>" + startDate + "<br>" + endDate + "<br>" + groupBy + "</p>";

                String dataTypeForHeading;
                if (dataType.compareToIgnoreCase("avg") == 0) {
                    dataTypeForHeading = "Average";
                }
                else if (dataType.compareToIgnoreCase("sum") == 0) {
                    dataTypeForHeading = "Total";
                }
                else if (dataType.compareToIgnoreCase("min") == 0) {
                    dataTypeForHeading = "Minimum";
                }
                else {
                    dataTypeForHeading = "Maximum";
                }

                if (otherMetrics.isEmpty()) {
                    if (numberOfMetrics != null && !numberOfMetrics.trim().isEmpty()) {
                        numMetrics = Integer.parseInt(numberOfMetrics) + 1;
                    }

                    List<String> metrics = jdbc.getClimateMetrics();
                    ArrayList<String> groupedTimePeriods = jdbc.getGroupedTimePeriods(startDate, endDate, groupBy);

                    //Number of time periods per row
                    int numTimePeriods = groupBy.size();

                    ArrayList<Metric> allMetrics = jdbc.getMetricComparisonData(metrics, dataType, startDate, endDate, groupBy);
                    allMetrics = getPercChangeOfMetrics(allMetrics);
                    allMetrics = getPercDifferenceOfMetrics(allMetrics, referenceClimateMetric);
                    allMetrics = sortArrayList(allMetrics);

                    //Perc Changes Table : Number of Metrics
                    html = html + "<h3>Percentage Changes Table</h3>";
                    html = html + "<div class='scrollabelContainer'>";
                    html = html + "<table class='scrollableTable' id='percChangesTable1'>";

                    html = html + "<tr>";
                    html = html + "<th>Metric Name</th>";
                    html = html + "<th>Average Percentage Change (%)</th>";
                    html = html + "<th>Percentage Difference From " + referenceClimateMetric + "</th>";
                    html = html + "</tr>";

                    
                    for (int i = 0; i < numMetrics; ++i) {
                        html = html + "<tr>";
                        html = html + "<td>" + allMetrics.get(i).getMetricName() + "</td>";
                        html = html + String.format("<td>%.2f</td>" , allMetrics.get(i).getAvgPercChange());
                        html = html + String.format("<td>%.2f</td>" , allMetrics.get(i).getPercDifference());
                        html = html + "</tr>";
                    }
                    

                    html = html + "</table></div><br>";


                    //Metric Comparison Table : Number of Metrics
                    html = html + "<h3>Metric Comparison Table</h3>";
                    html = html + "<div class='scrollabelContainer'>";
                    html = html + "<table class='scrollableTable' id='metricComparisonTable1'>";
                    html = html + "<tr>";
                    for (int i = 0; i < groupBy.size(); ++i) {
                        html = html + "<th>" + groupBy.get(i) + "</th>";
                    }
                    for (int i = 0; i < numMetrics; ++i) {
                        html = html + "<th>" + dataTypeForHeading + " " + allMetrics.get(i).getMetricName() + "</th>";
                    }
                    html = html + "</tr>";
                    
                    for (int i = 0; i < allMetrics.get(0).getValues().size(); ++i) {
                        html = html + "<tr>";
                        for (int j = 0; j < numTimePeriods; ++j) {
                            html = html + "<td>" + groupedTimePeriods.get(i * numTimePeriods + j) + "</td>";
                        }
                    
                        for (int k = 0; k < numMetrics; ++k) {
                            html = html + String.format("<td>%.2f</td>", allMetrics.get(k).getValues().get(i));
                        }
                        html = html + "</tr>";    
                    }
                    
                    html = html + "</table></div>";

                }

                else {
                    ArrayList<String> groupedTimePeriods = jdbc.getGroupedTimePeriods(startDate, endDate, groupBy);

                    ArrayList<Metric> allMetrics;
                    if (otherMetrics.contains("SelectAll")) {
                        otherMetrics = jdbc.getClimateMetrics();
                        if (!otherMetrics.contains(referenceClimateMetric)) {
                            otherMetrics.add(referenceClimateMetric);
                        }
                        allMetrics = jdbc.getMetricComparisonData(otherMetrics, dataType, startDate, endDate, groupBy);
                    }
                    

                    else {
                        boolean foundReferenceMetric = false;
                        for (int i = 0; i < otherMetrics.size(); ++i) {
                            if (otherMetrics.get(i).compareTo(referenceClimateMetric) == 0) {
                                foundReferenceMetric = true;
                                break;
                            }
                        }
                        if (!foundReferenceMetric) {
                            otherMetrics.add(referenceClimateMetric);
                        }
                        allMetrics = jdbc.getMetricComparisonData(otherMetrics, dataType, startDate, endDate, groupBy);
                    }
 
                    allMetrics = getPercChangeOfMetrics(allMetrics);
                    allMetrics = getPercDifferenceOfMetrics(allMetrics, referenceClimateMetric);
                    allMetrics = sortArrayList(allMetrics);

                    //Perc Changes Table : Specific metrics
                    html = html + "<h3>Percentage Changes Table</h3>";
                    html = html + "<div class='scrollabelContainer'>";
                    html = html + "<table class='scrollableTable'>";

                    html = html + "<tr>";
                    html = html + "<th>Metric Name</th>";
                    html = html + "<th>Average Percentage Change (%)</th>";
                    html = html + "<th>Percentage Difference From " + referenceClimateMetric + "</th>";
                    html = html + "</tr>";

                    
                    for (int i = 0; i < otherMetrics.size(); ++i) {
                        html = html + "<tr>";
                        html = html + "<td>" + allMetrics.get(i).getMetricName() + "</td>";
                        html = html + String.format("<td>%.2f</td>" , allMetrics.get(i).getAvgPercChange());
                        html = html + String.format("<td>%.2f</td>" , allMetrics.get(i).getPercDifference());
                        html = html + "</tr>";
                    }
                    

                    html = html + "</table></div><br>";

                    //Metric Comparison Table : Specific Metrics
                    html = html + "<h3>Metric Comparison Table</h3>";
                    html = html + "<div class='scrollabelContainer'>";
                    html = html + "<table  class='scrollableTable' id='metricComparisonTable2'>";
                    html = html + "<tr>";
                    for (int i = 0; i < groupBy.size(); ++i) {
                        html = html + "<th>" + groupBy.get(i) + "</th>";
                    }
                    for (int i = 0; i < otherMetrics.size(); ++i) {
                        html = html + "<th>" + dataTypeForHeading + " " + allMetrics.get(i).getMetricName() + "</th>";
                    }
                    html = html + "</tr>";
                    
                    for (int i = 0; i < allMetrics.get(0).getValues().size(); ++i) {
                        html = html + "<tr>";
                        for (int j = 0; j < groupBy.size(); ++j) {
                            html = html + "<td>" + groupedTimePeriods.get(i * groupBy.size() + j) + "</td>";
                        }
                    
                        for (int k = 0; k < otherMetrics.size(); ++k) {
                            html = html + String.format("<td>%.2f</td>", allMetrics.get(k).getValues().get(i));
                        }
                        html = html + "</tr>";    
                    }
                    
                    html = html + "</table></div>";

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


   //Updating avgPercChanges for each Metric in allMetrics ArrayList
  public ArrayList<Metric> getPercChangeOfMetrics(ArrayList<Metric> allMetrics){

    for (int i = 0; i < allMetrics.size(); ++i) {
        ArrayList<Double> values = allMetrics.get(i).getValues();
        ArrayList<Double> percChanges = new ArrayList<Double>();
        Double sumOfPercChanges = 0.0;
        for (int j = 0; j < values.size() - 1; ++j) {
            if (values.get(j) != 0.0) {
                percChanges.add((values.get(j+1) - values.get(j)) / values.get(j) * 100);
            }
            else {
                if (values.get(j+1) == 0.0) {
                    percChanges.add(0.0);
                }
                else {
                    percChanges.add(100.0);
                }
            }   
        }
        for (int j = 0; j < percChanges.size() ; ++j) {
            sumOfPercChanges += percChanges.get(j);
        }
        allMetrics.get(i).setAvgPercChange(sumOfPercChanges / percChanges.size());
    }
    
    return allMetrics;
  }


  //Updating percDifference of each Metric in ArrayList
  public ArrayList<Metric> getPercDifferenceOfMetrics(ArrayList<Metric> allMetrics, String referenceClimateMetric) {
    Double avgPercChangeOfReferenceMetric = 0.0;
    for (int i = 0; i < allMetrics.size(); ++i) {
        if (((allMetrics.get(i).getMetricName()).compareToIgnoreCase(referenceClimateMetric)) == 0) {
            avgPercChangeOfReferenceMetric = allMetrics.get(i).getAvgPercChange();
            break;
        }
    }

    for (int i = 0; i < allMetrics.size(); ++i) {
        Double percDifference = allMetrics.get(i).getAvgPercChange() - avgPercChangeOfReferenceMetric;
        allMetrics.get(i).setPercDifference(percDifference);
    }

    return allMetrics;
  }

  //Sorting ArrayList
  public ArrayList<Metric> sortArrayList(ArrayList<Metric> allMetrics) {
    int i;
    int j;
    Metric temp;

    for (i = 1; i < allMetrics.size(); ++i) {
        j = i;
        while (j > 0 && (Math.abs(allMetrics.get(j).getPercDifference()) < Math.abs(allMetrics.get(j-1).getPercDifference())) ) {
            temp = allMetrics.get(j);
            allMetrics.set(j, allMetrics.get(j-1));
            allMetrics.set(j-1, temp);
            --j;
        }
    }


    return allMetrics;
  }

}

