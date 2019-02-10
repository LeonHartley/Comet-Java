package com.cometproject.website.website.routes;

import org.apache.commons.io.IOUtil;
import org.apache.commons.lang.StringUtils;
import spark.Request;
import spark.Response;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CameraRoute {
    public static String upload(final Request request, final Response response) {
        try {
            final byte[] imageData = IOUtil.toByteArray(request.raw().getInputStream());
            final String imageName = request.params("photoId");

            // Create the image file.
            try {
                String location = "./camera-images/" + imageName + ".png";
//todo: put this in config
                if (StringUtils.isNumeric(imageName)) {
                    location = "C:\\inetpub\\wwwroot\\comet\\swf\\c_images\\navigator-thumbnail\\" + imageName + ".png";
                }

                final FileOutputStream outputStream = new FileOutputStream(location);

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
