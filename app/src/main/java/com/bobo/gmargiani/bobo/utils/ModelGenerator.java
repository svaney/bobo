package com.bobo.gmargiani.bobo.utils;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.app.App;
import com.bobo.gmargiani.bobo.model.KeyValue;
import com.bobo.gmargiani.bobo.model.OwnerDetails;
import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.rest.ApiResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

public class ModelGenerator {
    final static Random generator = new Random();

    public static ApiResponse<Boolean> generateTokeResponse() {
        ApiResponse<Boolean> resp = new ApiResponse<>();
        resp.setCode("0");
        resp.setResult(false);
        return resp;
    }

    static int itemCount;

    public static ArrayList<String> imageUrls = new ArrayList<String>() {{
        add("https://thehoovercardinal.org/wp-content/uploads/2018/02/Kendrick.jpg");
        add("https://wallpaperbrowse.com/media/images/_89716241_thinkstockphotos-523060154.jpg");
        add("https://vignette.wikia.nocookie.net/dragonballfanon/images/7/70/Random.png/revision/latest?cb=20161221030547");
        add("http://kb4images.com/images/random-image/37034707-random-image.jpg");
        add("https://i.ytimg.com/vi/dRclC4ZnWwU/maxresdefault.jpg");
        add("http://patriots.ge/wp-content/uploads/2016/03/team-irma-2.jpg");
        add("https://static.independent.co.uk/s3fs-public/styles/article_small/public/thumbnails/image/2017/05/26/16/harambe-main.jpg");
        add("http://archives.frederatorblogs.com/random/files/2008/04/b-corrupted-by-random-noise-bit-error-rate0020.thumbnail.gif");
        add("http://www.vitamin-ha.com/wp-content/uploads/2014/04/VH-Random-duckhourse.jpg");
        add("http://archives.frederatorblogs.com/random/files/2008/04/b-corrupted-by-random-noise-bit-error-rate0020.thumbnail.gif");
    }};

    public static ApiResponse<ArrayList<StatementItem>> generateStatements(int count) {
        ArrayList<StatementItem> items = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            StatementItem item = new StatementItem();
         //   item.setOwnerId(generator.nextInt(2));
            item.setDescription(App.getInstance().getString(R.string.lorem));
            item.setTitle("Nivti: " + itemCount++);
      //      item.setStatementId(itemCount);
            item.setCategory("CL");
            item.setSelling(true);

            long range = 252460800L;
          //  item.setCreateDate((long) (generator.nextDouble() * range) + 1262289600L);

            int price = generator.nextInt(9990);

            item.setPrice(new BigDecimal(price).divide(new BigDecimal(100)));
            item.setImages(new ArrayList<String>() {{
                add(imageUrls.get(generator.nextInt(imageUrls.size())));
                add(imageUrls.get(generator.nextInt(imageUrls.size())));
                add(imageUrls.get(generator.nextInt(imageUrls.size())));
                add(imageUrls.get(generator.nextInt(imageUrls.size())));
            }});

            item.setLocation("TB");

            items.add(item);
        }

        ApiResponse<ArrayList<StatementItem>> response = new ApiResponse<>();
        response.setCode("0");
        response.setMessage("OK");
        response.setResult(items);
        return response;
    }

    public static ApiResponse<ArrayList<KeyValue>> getLocations() {
        ArrayList<KeyValue> values = new ArrayList<>();

        values.add(new KeyValue("TB", "Tbilisi"));
        values.add(new KeyValue("GR", "Gori"));
        values.add(new KeyValue("BT", "Batumi"));
        values.add(new KeyValue("KT", "Kutaisi"));
        values.add(new KeyValue("MS", "Mestia"));

        ApiResponse<ArrayList<KeyValue>> response = new ApiResponse<>();
        response.setCode("0");
        response.setResult(values);
        response.setMessage("OK");
        return response;
    }

    public static ApiResponse<ArrayList<KeyValue>> getCategories() {
        ArrayList<KeyValue> values = new ArrayList<>();

        values.add(new KeyValue("AM", "Auto/Moto"));
        values.add(new KeyValue("CM", "Computers"));
        values.add(new KeyValue("PR", "Parfume"));
        values.add(new KeyValue("CL", "Clothing"));
        values.add(new KeyValue("MS", "Music"));


        ApiResponse<ArrayList<KeyValue>> response = new ApiResponse<>();
        response.setCode("0");
        response.setResult(values);
        response.setMessage("OK");
        return response;

    }

    public static ApiResponse<OwnerDetails> getOwnerDetails(long ownerId) {
        OwnerDetails details = new OwnerDetails();
        details.setCompany(false);
        details.setLocation("TB");
       // details.setOwnerId(ownerId);
        details.setOwnerName("Zuraba");
        details.setOwnerSecondName("Machavariani");
        if (ownerId == 0) {
            details.setAvatar("http://plus.kvira.ge/wp-content/uploads/2016/10/davit_tarxan_mouravi_0410.jpg");
        } else {
            details.setAvatar("http://es.me/wp-content/uploads/2017/08/Olofmeister-FaZe-Clan-from-Fnatic-817x320.jpeg");
        }
        details.setPhone("+995 98 19 41 17");
        ApiResponse<OwnerDetails> response = new ApiResponse<>();
        response.setCode("0");
        response.setMessage("OK");
        response.setResult(details);
        return response;
    }

    public static ApiResponse<ArrayList<OwnerDetails>> getOwnerDetails(int count) {
        ApiResponse<ArrayList<OwnerDetails>> response = new ApiResponse<>();
        response.setCode("0");
        response.setMessage("OK");

        ArrayList<OwnerDetails> details = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            details.add(getOwnerDetails((long) i / 2).getResult());
        }
        response.setResult(details);
        return response;
    }


}
