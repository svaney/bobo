package com.bobo.gmargiani.bobo.utils;

import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.rest.ApiResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

public class ModelGenerator {
    public static ApiResponse<Boolean> generateTokeResponse() {
        ApiResponse<Boolean> resp = new ApiResponse<>();
        resp.setCode("0");
        resp.setResult(false);
        return resp;
    }

    static int itemCount;

    private static ArrayList<String> imageUrls = new ArrayList<String>() {{
        add("https://vignette.wikia.nocookie.net/blogclan-2/images/4/45/Random-turtle.gif/revision/latest?cb=20160706220110");
        add("https://wallpaperbrowse.com/media/images/_89716241_thinkstockphotos-523060154.jpg");
        add("https://vignette.wikia.nocookie.net/dragonballfanon/images/7/70/Random.png/revision/latest?cb=20161221030547");
        add(null);
        add("http://kb4images.com/images/random-image/37034707-random-image.jpg");
        add("https://i.ytimg.com/vi/dRclC4ZnWwU/maxresdefault.jpg");
        add("http://patriots.ge/wp-content/uploads/2016/03/team-irma-2.jpg");
        add("https://static.independent.co.uk/s3fs-public/styles/article_small/public/thumbnails/image/2017/05/26/16/harambe-main.jpg");
        add("http://archives.frederatorblogs.com/random/files/2008/04/b-corrupted-by-random-noise-bit-error-rate0020.thumbnail.gif");
        add("http://www.vitamin-ha.com/wp-content/uploads/2014/04/VH-Random-duckhourse.jpg");
    }};

    public static ApiResponse<ArrayList<StatementItem>> generateStatements(int count) {
        ArrayList<StatementItem> items = new ArrayList<>();
        final Random generator = new Random();
        for (int i = 0; i < count; i++) {
            StatementItem item = new StatementItem();
            item.setDescription("aq aris agcerili ranairia da ravaria simon es saqoneli");
            item.setTitle("Nivti: " + itemCount++);
            item.setPrice(new BigDecimal(2.5));
            item.setImages(new ArrayList<String>() {{
                add(imageUrls.get(generator.nextInt(imageUrls.size())));
            }});
            items.add(item);
        }

        ApiResponse<ArrayList<StatementItem>> response = new ApiResponse<>();
        response.setCode("0");
        response.setResult(items);
        return response;
    }
}
