package com.cometproject.website.website.routes;

import org.apache.commons.io.IOUtil;
import spark.Request;
import spark.Response;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class CameraRoute {
    public static String upload(final Request request, final Response response) {
        try {
            final byte[] imageData = IOUtil.toByteArray(request.raw().getInputStream());
            final String ssoTicket = request.params("ssoTicket");
            final UUID imageId = UUID.randomUUID();

            // Create the image file.
            try {
                final FileOutputStream outputStream = new FileOutputStream("./camera-images/" + imageId.toString() + ".png");

                outputStream.write(imageData);
                outputStream.close();
            } catch (Exception e) {
                // Failed to save the image!
                return "error.img_save";
            }

            // Submit the API request to the emulator with the new image data!
            return "OK";
        } catch (Exception e) {
            return "error.img_save";
        }
    }

    public static Object photo(final Request request, final Response response) {
        final UUID photoId = UUID.fromString(request.params("photoId"));

        response.type("image/png");

        try {
            final byte[] imageData = Files.readAllBytes(Paths.get("./camera-images/" + photoId.toString() + ".png"));

            IOUtil.copy(imageData, response.raw().getOutputStream());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
