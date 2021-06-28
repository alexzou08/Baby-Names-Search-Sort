/**
 * Print out total number of babies born, as well as for each gender, in a given CSV file of baby name data.
 * 
 * @author Duke Software Team 
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;

public class BabyBirths {
    public void printNames () {
        FileResource fr = new FileResource();
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            if (numBorn <= 100) {
                System.out.println("Name " + rec.get(0) +
                           " Gender " + rec.get(1) +
                           " Num Born " + rec.get(2));
            }
        }
    }

    public void totalBirths (FileResource fr) {
        int totalBirths = 0;
        int totalBoys = 0;
        int totalGirls = 0;
        int numOfBoysNames = 0;
        int numOfGirlsNames = 0;
        int numOfTotalNames = 0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            totalBirths += numBorn;
            if (rec.get(1).equals("M")) {
                totalBoys += numBorn;
                numOfBoysNames += 1;
                numOfTotalNames += 1;
            }
            else {
                totalGirls += numBorn;
                numOfGirlsNames += 1;
                numOfTotalNames += 1;
            }
        }
        System.out.println("total births = " + totalBirths);
        System.out.println("female girls births = " + totalGirls);
        System.out.println("male boys births = " + totalBoys);
        System.out.println("total names = " + numOfTotalNames);
        System.out.println("number of girls names = " + numOfGirlsNames);
        System.out.println("number of boys names = " + numOfBoysNames);
    }

    public void testTotalBirths () {
        FileResource fr = new FileResource();
        //FileResource fr = new FileResource("data/yob2014.csv");
        totalBirths(fr);
    }
    
    public int getRank(int year, String name, String gender) {
        int rank = 0;
        Boolean appear = false;
        String fileName = "data/yob" + year + ".csv";
        //String fileName = "data/yob2012.csv";
        FileResource fr = new FileResource(fileName);
        for (CSVRecord rec : fr.getCSVParser(false)) {
            if(rec.get(1).equals(gender)) {
                rank += 1;
                if(rec.get(0).equals(name)) {
                    appear = true;
                    break;
                }
            }
        }    
        if (appear == false) {
            rank = -1;
        }
        return rank;
    }
    
    public void testGetRank() {
        int rank = getRank(1971, "Frank", "M");
        System.out.println("Rank is " + rank);
    }
    
    public String getName(int year, int rank, String gender) {
        String name = "NO NAME";
        int currentRank = 0;
        Boolean appear = false;
        String fileName = "data/yob" + year + ".csv";
        FileResource fr = new FileResource(fileName);
        for(CSVRecord rec : fr.getCSVParser(false)) {
            if(rec.get(1).equals(gender)) {
                currentRank +=1;
                if(currentRank == rank) {
                    name = rec.get(0);
                    break;
                }
            }
        }
        return name;
    }
    
    public void testGetName() {
        String name = getName(1982, 450, "M");
        System.out.println("Name is " + name);
    }
    
    public void whatIsNameInYear(String name, int year, int newYear, String gender) {
        int rank = getRank(year, name, gender);
        String newName = getName(newYear, rank, gender);
        System.out.println(name + " born in " + year + " would be " + newName 
                           + " if they were born in " + year + ".");
    }
    
    public void testWhatIsNameInYear() {
        whatIsNameInYear("Owen", 1974, 2014, "M");
    }
    
    public int yearOfHighestRank(String name, String gender) {
        int highestYear = -1;
        int highestRank = 99999;
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            //FileResource fr = new FileResource(f);
            String fileName = f.getName ();
            int currentYear = Integer.parseInt(fileName.substring(3,7));
            int currentRank = getRank(currentYear, name, gender);
            if (currentRank < highestRank && currentRank > 0) {
                highestRank = currentRank;
                highestYear = currentYear;
            }
        }
        return highestYear;
    }
    
    public void testYearOfHighestRank() {
        int year = yearOfHighestRank("Mich", "M");
        System.out.println(year);
    }
    
    public double getAverageRank(String name, String gender) {
        double averageRank = -1.0;
        double totalRank = 0;
        int numberOfFiles = 0;
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            //FileResource fr = new FileResource(f);
            String fileName = f.getName ();
            int currentYear = Integer.parseInt(fileName.substring(3,7));
            int currentRank = getRank(currentYear, name, gender);
            totalRank += currentRank;
            numberOfFiles += 1;
        }        
        averageRank = totalRank/numberOfFiles;
        return averageRank;
    }
    
    public void testGetAverageRank() {
        double averageRank = getAverageRank("Robert", "M");
        System.out.println(averageRank);
    }    
    
    public int getTotalBirthsRankedHigher(int year, String name, String gender) {
        int totalBirth = 0;
        int currentBirths = 0;
        //year = 2012;
        FileResource fr = new FileResource("data/yob" + year + ".csv");
        int rank = getRank(year, name, gender);
        for (int i = 1; i < rank; i ++) {
            String currentName = getName(year, i, gender);
            for (CSVRecord rec : fr.getCSVParser(false)) {
                if(rec.get(0).equals(currentName) && rec.get(1).equals(gender)) {
                    currentBirths = Integer.parseInt(rec.get(2));
                    break;
                } else {
                    currentBirths = 0;
                }
            }
            totalBirth += currentBirths;
        }
        return totalBirth;
    }
    
    public void testGetTotalBirthsRankedHiger() {
        int total = getTotalBirthsRankedHigher(2012, "Isabella", "F");
        System.out.println(total);
    }
}
