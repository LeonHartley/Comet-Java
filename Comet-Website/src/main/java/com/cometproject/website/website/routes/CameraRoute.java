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
            final String imageName = request.params("photoId");

            // Create the image file.
            try {
                final FileOutputStream outputStream = new FileOutputStream("C:\\Websites\\Libbo\\cdn\\swf\\c_images\\navigator-thumbnail\\" + imageName + ".png");

                outputStream.write(imageData);
                outputStream.close();
            } catch (Exception e) {
                // Failed to save the image!
                return "error.img_save";
            }

            return "OK";// imageId.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "error.img_save";
        }
    }

    public static Object photo(final Request request, final Response response) {
        final String photoIdParam = request.params("photoId");
        final String photoId = photoIdParam.replace("_small", "").split("\\.")[0];

        response.status(200);
        response.type("image/png");

        response.header("Cache-Control", "no-cache, no-store, must-revalidate");
        response.header("Pragma", "no-cache");
        response.header("Expires", "0");

        try {
            final byte[] imageData = Files.readAllBytes(Paths.get("./camera-images/" + photoId + ".png"));

            IOUtil.copy(imageData, response.raw().getOutputStream());
            return "";
        } catch (Exception e) {
            response.status(404);
            return "";
        }
    }

}
