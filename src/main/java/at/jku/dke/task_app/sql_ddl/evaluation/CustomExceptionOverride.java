package at.jku.dke.task_app.sql_ddl.evaluation;
//import com.zaxxer.hikari.util.ExceptionOverrideDelegate;
//
//import java.sql.SQLException;
//
//public class CustomExceptionOverride implements ExceptionOverrideDelegate {
//    @Override
//    public boolean shouldOverrideException(SQLException sqlException) {
//        if (sqlException instanceof PSQLException && "0A000".equals(sqlException.getSQLState())) {
//            System.out.println("Ignoring unsupported SQLSTATE 0A000 error: " + sqlException.getMessage());
//            return true; // Ignore this specific exception
//        }
//        return false; // Let HikariCP handle other exceptions normally
//    }
//}
