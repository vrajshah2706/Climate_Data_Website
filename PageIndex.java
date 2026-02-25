package app;

import java.util.ArrayList;

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
 * @editor David Eccles, 2025. email: david.eccles@rmit.edu.au
 */
public class PageIndex implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Header information
        html = html + "<head>" + 
                "<title>Home</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        html = html + "<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css'>";
        html = html + "<script src='https://cdn.jsdelivr.net/npm/chart.js'></script>";
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
                <h1>Understanding Climate Change</h1>
            </div>
        """;

        // Add Div for page Content
        html = html + "<div class='content'>";

        // Add HTML for the page content
        html = html + """

            <div class='chart-container'>
                <h2>Temperature Trends</h2>
                <canvas id='temperatureChart'></canvas>
            </div>

            <div class='stats-container'>
                <div class="stat-card">
                    <h3>Year Range</h3>
                    <p>1970-2020</p>
                </div>
                <div class="stat-card">
                    <h3>Lowest Temperature</h3>
                    <p>Charlotte Pass, NSW -23&deg;C</p>
                </div>
                <div class="stat-card">
                    <h3>highest Rainfall</h3>
                    <p>Crohamhurst, QLA 907mm</p>
                </div>
            </div>
            """;

        // Get temperature data
        JDBCConnection jdbc = new JDBCConnection();
        ArrayList<String> temperatureData = jdbc.getAverageTemperatureByYear();
        
        // Creating the chart
        html = html + """
            <script>
                const ctx = document.getElementById('temperatureChart');
                
                // Prepare data arrays
                const years = [];
                const temperatures = [];
        """;
        
        // Add data points
        for (int i = 0; i < temperatureData.size(); i += 2) {
            html = html + "years.push('" + temperatureData.get(i) + "');\n";
            html = html + "temperatures.push(" + temperatureData.get(i + 1) + ");\n";
        }
        
        html = html + """
                new Chart(ctx, {
                    type: 'line',
                    data: {
                        labels: years,
                        datasets: [{
                            label: 'Average Temperature (°C)',
                            data: temperatures,
                            borderColor: 'rgb(75, 192, 192)',
                            tension: 0.1,
                            fill: false
                        }]
                    },
                    options: {
                        responsive: true,
                        plugins: {
                            title: {
                                display: true,                            
                                },
                            tooltip: {
                                callbacks: {
                                    label: function(context) {
                                        return 'Year: ' + context.label + ', Temperature: ' + context.raw.toFixed(2) + '°C';
                                    }
                                }
                            }
                        },
                        scales: {
                            y: {
                                beginAtZero: false,
                                title: {
                                    display: true,
                                    text: 'Temperature (°C)'
                                }
                            },
                            x: {
                                title: {
                                    display: true,
                                    text: 'Year'
                                },
                                ticks: {
                                    callback: function(value) {
                                        return years[value];
                                    }
                                }
                            }
                        }
                    }
                });
            </script>
        """;

        // Close Content div
        html = html + "</div>";


        // Finish the HTML webpage
        html = html + "</body>" + "</html>";


        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }
}
