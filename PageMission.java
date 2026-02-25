package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
public class PageMission implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/mission.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html = html + "<head>" + 
               "<title>Our Mission</title>" 
               +
               "<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css'></link>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        //html = html + "<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css'>";
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
                                <a href='page2A.html'>Sub Task 2.A</a>
                                <a href='page2B.html'>Sub Task 2.B</a>
                                <a href='page2C.html'>Sub Task 2.C</a>
                                <a href='page3A.html'>Sub Task 3.A</a>
                                <a href='page3B.html'>Sub Task 3.B</a>
                                <a href='page3C.html'>Sub Task 3.C</a>
                            </div>
                        </div>
                    </div>
                </div>
        """;

        // Add header content block
        html = html + """
            <div class='header'>
                <h1>Our Mission</h1>
            </div>
        """;

        // Add Div for page Content
        html = html + "<div class='content'>";

        // Add HTML for the page content
        html = html + """
            <div class='TextAndCarousel-container'>
            <div class='text-content'>
            <div>
                <h2>How We Address Climate Change</h2>
                <p>Our website provides easy-to-understand climate data to help Australians:</p>
                <ul>
                    <li>Learn About Climate Change</li>
                    <li>Make Informed Decisions</li>
                    <li>Explore Data Your Way</li>
                </ul>    
            </div>

            <div>
                <h2>What You Can Do on Our Website</h2>
                <ul>
                    <li><a href='/page2A.html'>Find Weather Stations Near You</a></li>
                    <li><a href='/page2B.html'>Check Weather Trends</a></li>
                    <li><a href='/page2C.html'>Spot Data Issues</a></li>
                    <li><a href='/page3A.html'>Compare Local Weather Stations</a></li>
                    <li><a href='/page3B.html'>See How Weather Factors Connect</a></li>
                    <li><a href='/page3C.html'>Track Long-Term Changes</a></li>
                </ul>    
            </div>
            </div>

            <div class='carousel-container'>
            <div id='myCarousel' class='carousel slide' data-ride='carousel'>

                <ol class='carousel-indicators'>
                    <li data-target='#myCarousel' data-slide-to='0' class='active'></li>
                    <li data-target='#myCarousel' data-slide-to='1'></li>
                    <li data-target='#myCarousel' data-slide-to='2'></li>
                </ol>

                <div class='carousel-inner'>
                    <div class='item active'>
                        <img src='Farmer.png' alt='A farmer standing in a field, using his mobile phone to check rising temperatures'>
                    </div>

                    <div class='item'>
                        <img src='Teen.png' alt='A high schooler checking a weather graph in his laptop for his school work'>
                    </div>

                    <div class='item'>
                        <img src='Climate Scientist.png' alt='A climate researcher analysing multi-station trend maps'>
                    </div>
                </div>

                <a class='left carousel-control' href='#myCarousel' data-slide='prev'>
                    <span class='glyphicon glyphicon-chevron-left'></span>
                    <span class='sr-only'>Previous</span>
                </a>
                <a class='right carousel-control' href='#myCarousel' data-slide='next'>
                    <span class='glyphicon glyphicon-chevron-right'></span>
                    <span class='sr-only'>Next</span>
                </a>
            </div>
            </div>

            </div>

            """;

            JDBCConnection jdbc = new JDBCConnection();
            html = html + """
            <div>
                <h2>Key Users We Target</h2>
                <div class='personaBoxes'>
            """;

            
            //Persona 1
            html = html + "<div class='personaBox'><div><strong>Persona 1 : " + jdbc.getPersonaInformation(1, "Role") + "</strong></div><br>";
            html = html + "<div>Name : " + jdbc.getPersonaInformation(1, "Name") + "</div><br>";
            html = html + "<div>Age : " + jdbc.getPersonaInformation(1, "Age") + "</div><br>";
            html = html + "<div>Gender : " + jdbc.getPersonaInformation(1, "Gender") + "</div><br>";
            html = html + "<div>Ethnicity : " + jdbc.getPersonaInformation(1, "Ethnicity") + "</div><br>";
            html = html + "<div>Background : " + jdbc.getPersonaInformation(1, "Background") + "</div><br>";
            html = html + "<div>Needs : ";
            String needs = jdbc.getPersonaInformation(1, "Needs");
            for (int i = 0; i < needs.length(); ++i) {
                if (needs.charAt(i) == '-') {
                    html = html + needs.substring(0, i) + "<br>";
                    needs = needs.substring(i);
                }
            }
            html = html + needs + "</div><br>";
            html = html + "<div>Goals : ";
            String goals = jdbc.getPersonaInformation(1, "Goals");
            for (int i = 0; i < goals.length(); ++i) {
                if (goals.charAt(i) == '-') {
                    html = html + goals.substring(0, i) + "<br>";
                    goals = goals.substring(i);
                }
            }
            html = html + "</div><br>";
            html = html + "<div>Skills and Expertise : ";
            String skills = jdbc.getPersonaInformation(1, "Skills_and_expertise");
            for (int i = 0; i < skills.length(); ++i) {
                if (skills.charAt(i) == '-') {
                    html = html + skills.substring(0, i) + "<br>";
                    skills = skills.substring(i);
                }
            }
            html = html + "</div><br></div>";

            //Persona 2
            html = html + "<div class='personaBox'><div><strong>Persona 2 : " + jdbc.getPersonaInformation(2, "Role") + "</strong></div><br>";
            html = html + "<div>Name : " + jdbc.getPersonaInformation(2, "Name") + "</div><br>";
            html = html + "<div>Age : " + jdbc.getPersonaInformation(2, "Age") + "</div><br>";
            html = html + "<div>Gender : " + jdbc.getPersonaInformation(2, "Gender") + "</div><br>";
            html = html + "<div>Ethnicity : " + jdbc.getPersonaInformation(2, "Ethnicity") + "</div><br>";
            html = html + "<div>Background : " + jdbc.getPersonaInformation(2, "Background") + "</div><br>";
            html = html + "<div>Needs : ";
            needs = jdbc.getPersonaInformation(2, "Needs");
            for (int i = 0; i < needs.length(); ++i) {
                if ((needs.charAt(i) == '-') && (i != 5)) {
                    html = html + needs.substring(0, i) + "<br>";
                    needs = needs.substring(i);
                }
            }
            html = html + needs + "</div><br>";
            html = html + "<div>Goals : ";
            goals = jdbc.getPersonaInformation(2, "Goals");
            for (int i = 0; i < goals.length(); ++i) {
                if (goals.charAt(i) == '-') {
                    html = html + goals.substring(0, i) + "<br>";
                    goals = goals.substring(i);
                }
            }
            html = html + "</div><br>";
            html = html + "<div>Skills and Expertise : ";
            skills = jdbc.getPersonaInformation(2, "Skills_and_expertise");
            for (int i = 0; i < skills.length(); ++i) {
                if (skills.charAt(i) == '-') {
                    html = html + skills.substring(0, i) + "<br>";
                    skills = skills.substring(i);
                }
            }
            html = html + "</div><br></div>";

            //Persona 3
            html = html + "<div class='personaBox'><div><strong>Persona 3 : " + jdbc.getPersonaInformation(3, "Role") + "</strong></div><br>";
            html = html + "<div>Name : " + jdbc.getPersonaInformation(3, "Name") + "</div><br>";
            html = html + "<div>Age : " + jdbc.getPersonaInformation(3, "Age") + "</div><br>";
            html = html + "<div>Gender : " + jdbc.getPersonaInformation(3, "Gender") + "</div><br>";
            html = html + "<div>Ethnicity : " + jdbc.getPersonaInformation(3, "Ethnicity") + "</div><br>";
            html = html + "<div>Background : " + jdbc.getPersonaInformation(3, "Background") + "</div><br>";
            html = html + "<div>Needs : ";
            needs = jdbc.getPersonaInformation(3, "Needs");
            for (int i = 0; i < needs.length(); ++i) {
                if (needs.charAt(i) == '-') {
                    html = html + needs.substring(0, i) + "<br>";
                    needs = needs.substring(i);
                }
            }
            html = html + needs + "</div><br>";
            html = html + "<div>Goals : ";
            goals = jdbc.getPersonaInformation(3, "Goals");
            for (int i = 0; i < goals.length(); ++i) {
                if (goals.charAt(i) == '-') {
                    html = html + goals.substring(0, i) + "<br>";
                    goals = goals.substring(i);
                }
            }
            html = html + "</div><br>";
            html = html + "<div>Skills and Expertise : ";
            skills = jdbc.getPersonaInformation(3, "Skills_and_expertise");
            for (int i = 0; i < skills.length(); ++i) {
                if (skills.charAt(i) == '-') {
                    html = html + skills.substring(0, i) + "<br>";
                    skills = skills.substring(i);
                }
            }
            html = html + "</div><br></div>";


            html = html + """
                </div>
            </div>

            <div>
                <h5 id='AboutUsText'><center>About us</center></h2>
                    <div class='AboutUsBoxes'>

                    """;

            html = html + "<div class='AboutUsBox'>Member 1<br>";
            html = html + "Name : " + jdbc.getMemberInformation(1, "Name") + "<br>";
            html = html + "Student ID : " + jdbc.getMemberInformation(1, "StudentId") + "<br>";
            html = html + "Subtask : " + jdbc.getMemberInformation(1, "Subtask") + "<br></div>";

            html = html + "<div class='AboutUsBox'>Member 2<br>";
            html = html + "Name : " + jdbc.getMemberInformation(2, "Name") + "<br>";
            html = html + "Student ID : " + jdbc.getMemberInformation(2, "StudentId") + "<br>";
            html = html + "Subtask : " + jdbc.getMemberInformation(2, "Subtask") + "<br></div>";

            html = html + "<div class='AboutUsBox'>Member 3<br>";
            html = html + "Name : " + jdbc.getMemberInformation(3, "Name") + "<br>";
            html = html + "Student ID : " + jdbc.getMemberInformation(3, "StudentId") + "<br>";
            html = html + "Subtask : " + jdbc.getMemberInformation(3, "Subtask") + "<br></div>";
                            

            html = html + """
                    
                </div>
            </div>

            """;
        
        

        // Close Content div
        html = html + "</div>";

        // Footer
        html = html + """
            <div class='footer'>
                <p>COSC2803 - Studio Project Starter Code (ACC-Apr2025)</p>
            </div>
        """;

        // Finish the HTML webpage
        html = html + "<script src='https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js'></script>";
        html = html + "<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js'></script>";
        html = html + "</body>" + "</html>";
        

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

    



}



