package ru.aleks.jdbc.starter.connect;

import ru.util.ConnectionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;

public class BlobRunner {
    public static void main(String[] args) throws SQLException, IOException {
        // blob - bytea
        // clob - Text
        //saveImage();
        getImage();
    }


    private static void getImage() throws SQLException, IOException {
        var sql = """
                select image from aircraft where id = ?
                """;
        try (var connect = ConnectionManager.open();
             var preper = connect.prepareStatement(sql)) {
            preper.setInt(1, 1);
            var rezult = preper.executeQuery();
            if (rezult.next()) {
                var image = rezult.getBytes("image");
                Files.write(Path.of("resources", "boeing777new .jpg"), image, StandardOpenOption.CREATE);
            }

        }
    }


    private static void saveImage() throws SQLException, IOException {
        var sql = """
                UPDATE aircraft SET image = ? WHERE id = 1
                """;

        try (var connect = ConnectionManager.open();
             var preper = connect.prepareStatement(sql)) {

            preper.setBytes(1, Files.readAllBytes(Path.of("resources", "boeing777.jpg")));
            preper.executeUpdate();


        }
    }


//    private static void saveImage() throws SQLException {
//        var sql = """
//                UPDATE aircraft set image = ? where id = 1
//                """;
//        try (var connect = ConnectionManager.open();
//        var preper = connect.prepareStatement(sql)){
//            connect.setAutoCommit(false);
//          var blob = connect.createBlob();
//          blob.setBytes(1, Files.readAllBytes(Path.of("resources","boeing777.jpg")));
//          preper.setBlob(1,blob);
//          preper.executeUpdate();
//          connect.commit();
//
//        }
//    }

}
