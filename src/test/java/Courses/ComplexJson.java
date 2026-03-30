package Courses;

import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import static Commens.Utlis.rawToJson;
import static PayLoads.CoursesPayload.getCoursesDetails;

import static PayLoads.CoursesPayload.getCoursesDetails;
import static org.testng.Assert.assertEquals;

public class ComplexJson {


    @Test
    public void ComplexJsonTest() {
        JsonPath js = rawToJson(getCoursesDetails());

        //Print Number of courses
        int CourseCount = js.getInt("courses.size()");
        System.out.println("Courses:" + CourseCount);
        assertEquals(CourseCount, 3, " Course count does not match expected value");

        //Print Purchase amount
        int purchaseAmount = js.getInt("dashboard.purchaseAmount");
        System.out.println("Purchases:" + purchaseAmount);

        //print first course title
        String expectedCourseTitle = "Selenium Python";
        String firstCourseTitle = js.getString("courses[0].title");
        System.out.println("First course title :" + firstCourseTitle);
        assertEquals(firstCourseTitle, expectedCourseTitle, "Course title does not match expected value");
        Assert.assertEquals(firstCourseTitle, "Selenium Python");

        //Print All course titles and their respective Prices
        for (int i = 0; i < CourseCount; i++) {
            String courseTitle = js.getString("courses[" + i + "].title");
            int coursePrice = js.getInt("courses[" + i + "].price");
            System.out.println("Course title is :" + courseTitle + " and price is :" + coursePrice);

            //Print no of copies sold by RPA Course
            for (int j = 0; j < CourseCount; j++) {
                if (courseTitle.equals("RPA")) {
                    int numOfCopies = js.getInt("courses[" + j + "].copies");
                    System.out.println("Number of RPA course copies:" + numOfCopies);
                    break;
                }

                // Verify if Sum of all Course prices matches with Purchase Amount
                int totalPrice = 0;

                int courseCount = js.getInt("courses.size()");
                for (int k = 0; k < courseCount; k++) {
                    int price = js.getInt("courses[" + k + "].price");
                    int copies = js.getInt("courses[" + k + "].copies");
                    totalPrice += price * copies;
                }

                assertEquals(totalPrice, purchaseAmount, "Total course prices do not match Purchase Amount");

                System.out.println("Total calculated amount: " + totalPrice);

            }
        }
    }
}