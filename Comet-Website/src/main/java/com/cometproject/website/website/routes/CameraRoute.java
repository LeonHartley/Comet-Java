package com.cometproject.website.website.routes;

import com.cometproject.website.api.ApiClient;
import org.apache.commons.io.IOUtil;
import spark.Request;
import spark.Response;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
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
            final Map<String, String> parameters = new HashMap<String, String>() {{
                put("ssoTicket", ssoTicket);
                put("photoId", imageId.toString());
            }};

            ApiClient.getInstance().execute("/camera/purchase", parameters);
            return "OK";
        } catch (Exception e) {
            return "error.img_save";
        }
    }

    public static Object photo(final Request request, final Response response) {
        final String photoIdParam = request.params("photoId");
        final UUID photoId = UUID.fromString(photoIdParam.replace("_small", "").split("\\.")[0]);

        response.status(200);
        response.type("image/png");

        try {
            final byte[] imageData = Files.readAllBytes(Paths.get("./camera-images/" + photoId.toString() + ".png"));

            IOUtil.copy(imageData, response.raw().getOutputStream());
            return "";
        } catch (Exception e) {
            response.status(404);
            return "";
        }
    }

}
