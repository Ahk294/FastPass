/*
                                            NAME: ABDUL HASEEB KHAN
                                                MISIS: M00792907
*/

import java.io.*;
import java.security.SecureRandom;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Random;


//      The Password class has the methods for generating the password and storing it into a file.
public class Password implements Serializable {
//    password class instance variables
    private Date gen_date;
    private String username, web_app, userPass;

//    char array to store the characters of the randomly generated password
    char[] pass = new char[50];

//    default constructor
    Password(){

    }

    Password(Date gen_date, String username, String web_app, String userPass){
        this.gen_date = gen_date;
        this.username = username;
        this.web_app = web_app;
        this.userPass = userPass;
    }

//    getter and setters
    public Date getGen_date() {
        return gen_date;
    }

    public void setGen_date(Date gen_date) {
        this.gen_date = gen_date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWeb_app() {
        return web_app;
    }

    public void setWeb_app(String web_app) {
        this.web_app = web_app;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

/*
        The difference between util.Random and security.SecureRandom is that SecureRandom generates more non-predictable
        random numbers than Random. This is because SecureRandom generates random numbers using Cryptographically Secure
        Pseudo-Random Number Generator (CSPRNG), whereas Random generates random numbers using Linear Congruential Generator (LCG).
*/
//      Using SecureRandom to generate secure passwords.
    Random rand = new SecureRandom();


//    method for generating a random password.
    public char[] generatePassword(int passLength, String allCharacters){
        for(int i = 0; i < passLength; i++){
            pass[i] = allCharacters.charAt(rand.nextInt(allCharacters.length()));
        }
        return pass;
    }

//    .fileIO() method helps in writing the user's generated password history into a .dat file and it also reads it into a .txt file.
    public static void fileIO(ArrayList<Password> passwords, String username) {
//        .dat file location.
        String datFile = "C:\\Users\\hasee\\Java FP App\\FP Users\\"+ username +".dat";
        String txtFile = "C:\\Users\\hasee\\Java FP App\\FP Users\\"+ username +" History.txt";
        try {
//            writing the objects to the .dat file.
            FileOutputStream fileOP = new FileOutputStream(datFile, false);
/*
        in order to read the written objects in normal text form (reconstitute), we have to write them in the .dat file using
        the ObjectOutputStream class. Later, using the ObjectInputStream class we can extract the objects from the .dat file and
        print them in a .txt file in readable form.

        In order to write my Password objects using the ObjectOutputStream, I had to make my Password class implement Serializable.
        This means that the objects of the Password class would now be converted to a byte stream so that this byte stream
        can be reverted back into a copy of the objects. This helps in printing the user's passwords in a text file.
 */
            ObjectOutputStream output = new ObjectOutputStream(fileOP);
            output.writeObject(passwords);

//            reading the objects from the file
            FileInputStream fileIN = new FileInputStream(datFile);
//            ObjectInputStream class helps in reconstituting the objects in the .dat file to normal text.
            ObjectInputStream input = new ObjectInputStream(fileIN);

//            storing the objects read by ObjectInputStream in an other ArrayList.
            ArrayList<Password> passwordArrayList = (ArrayList<Password>) (input.readObject());

//            declaring a FileWriter object with the .txt file location
            FileWriter passFile = new FileWriter(txtFile);

//            clearing the stream to make sure the file updates side by side with the database.
            passFile.flush();

//            printing the objects in text in a file.
            for(Password p:passwordArrayList){
//            using the .write method to write the password in the file
                passFile.write(p.gen_date + " -> " + p.web_app + ": " + p.userPass + "\n");
            }
//            closing the passFile
            passFile.close();

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
