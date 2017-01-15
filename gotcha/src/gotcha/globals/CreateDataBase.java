package gotcha.globals;

import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import javax.naming.*;

/**
 * Application Lifecycle Listener implementation class CreateDataBase
 *
 */
@WebListener
public class CreateDataBase implements ServletContextListener {

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event)  {
    	
    	Context context = null;
    	
		try {
			context = new InitialContext();
		} catch (NamingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
        Database database = new Database();
        
        try {
        	
        	database.execute("CREATE TABLE USERS ("
						+ 		"USERNAME 		VARCHAR(10) NOT NULL PRIMARY KEY,"
						+ 		"PASSWORD 		VARCHAR(8) NOT NULL,"
						+ 		"NICKNAME 		VARCHAR(20) UNIQUE,"
						+		"DESCRIPTION 	VARCHAR(50),"
						+ 		"PHOTO_URL 		VARCHAR(150),"
						+ 		"STATUS 		VARCHAR(6) NOT NULL,"
						+ 		"LAST_SEEN 		TIMESTAMP NOT NULL"
						+ 	  ")"
						     );

        	database.execute("CREATE TABLE CHANNELS ("
			 			+ 		"NAME 			VARCHAR(30) PRIMARY KEY,"
			 			+ 		"DESCRIPTION 	VARCHAR(500) NOT NULL,"
			 			+ 		"CREATED_BY 	VARCHAR(10) NOT NULL,"
			 			+ 		"CREATED_TIME 	TIMESTAMP NOT NULL,"
			 			+ 		"SUBSCRIBERS 	INTEGER"
			 			+ 	  ")"
					   	     );

        	database.execute("CREATE TABLE SUBSCRIPTIONS ("
						+ 		"ID 			INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,"
						+ 		"NICKNAME 		VARCHAR(20) NOT NULL REFERENCES USERS(NICKNAME) ON DELETE CASCADE,"
						+ 		"CHANNEL 		VARCHAR(30) NOT NULL REFERENCES CHANNELS(NAME) ON DELETE CASCADE"
			 			+ 	  ")"
				   	     	 );

        	database.execute("CREATE TABLE MESSAGES ("
						+ 		"ID 			INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,"
						+ 		"SENDER 		VARCHAR(20) NOT NULL REFERENCES USERS(NICKNAME) ON DELETE CASCADE,"
						+ 		"RECEIVER 		VARCHAR(30) NOT NULL,"
						+ 		"TEXT 			VARCHAR(500) NOT NULL,"
						+ 		"SENT_TIME 		TIMESTAMP NOT NULL"
						+ 	  ")"
						     );
        	
        	database.commit();
        	
        	context.bind(Globals.dbName, database);
        	
		} catch (SQLException e) {
			if (e.getSQLState().equals("X0Y32")) {
				System.out.println("The database is already existing, you're now connected to it.");
				try {
					context.bind(Globals.dbName, database);
				} catch (NamingException n) {
					n.printStackTrace();
				}
			} else {
				System.out.println("An unknown error has occured while trying to create the database.");
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent event)  {
    	try {
			Context context = new InitialContext();
			Database database = (Database)context.lookup(Globals.dbName);
			database.shutdown();
			context.unbind(Globals.dbName);
    	} catch (NamingException e) {
    		e.printStackTrace();
    	}
    }
}
