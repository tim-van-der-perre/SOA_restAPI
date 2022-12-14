package com.Create.API.rest.kafkaListener;

import com.Create.API.rest.Models.Product;
import com.Create.API.rest.Repo.ProductRepo;
import com.Create.API.rest.kafkaConfig.KafkaProducerConfig;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class KafkaConsumer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    KafkaProducerConfig kafkaProducerConfig = new KafkaProducerConfig();

    Product anime;
    JSONObject product;
    String type;
    long requestId;
    long param;
    List<Product> list;

    @Autowired
    public ProductRepo productRepo;

    /*
    @KafkaListener(topics = "kafka_example")
    public void consume(String message) {
        System.out.println("Consumed message:" + message);
    }

     */

    @KafkaListener(topics = "Anime-response", groupId = "groupId")
    void listenertest(String data){
        System.out.println("confirmation received");
        System.out.println(data);
    }

    //  LUISTEREN EN VERDELEN AAN HAND VAN TYPE REQUEST
    @KafkaListener(topics = "Anime-request", containerFactory = "productKafkaListenerFactory")
    public void anime_request(String data) throws JSONException {
        System.out.println("kafkalistener krijgt: " + data);
        JSONObject ob = new JSONObject(data);
        this.type = ob.getString("type");
        this.requestId = ob.getInt("requestId");
        switch (type){
            case "get":
                anime_request_get(ob);
                break;
            case "list":
                anime_request_list(ob);
                break;
            case "post":
                anime_request_post(ob);
                break;
            case "update":
                anime_request_update(ob);
                break;
            case "delete":
                anime_request_delete(ob);
                break;
        }
    }

    private void anime_request_delete(JSONObject ob) throws JSONException {
        try {

            long id = ob.getInt("param");
            this.anime = productRepo.findById(id).get();
            productRepo.deleteById(id);

            JSONObject message = new JSONObject();
            message.put("type", this.type);
            message.put("requestId", this.requestId);
            message.put("data", id);
            message.put("status", "ok");
            message.put("error", "null");
            System.out.println("anime_request_delete stuurt deze message: " + message);
            kafkaTemplate.send("anime-response", message.toString());
        } catch (Exception e) {
            int id = ob.getInt("param");
            JSONObject message = new JSONObject();
            message.put("type", this.type);
            message.put("requestId", this.requestId);
            message.put("param", id);
            message.put("status", "nok");
            message.put("error", "null");
            System.out.println("ERROR gevangen!!!! --> nok");
            System.out.println("anime_request_delete stuurt deze message: " + message);
            kafkaTemplate.send("anime-response", message.toString());

        }
    }

    private void anime_request_update(JSONObject ob) throws JSONException {
        try {
            System.out.println("1");
            this.product = ob.getJSONObject("param");
            System.out.println("2");
            long id = product.getLong("id");
            this.anime = productRepo.findById(id).get();
            System.out.println("3");
            String title = product.getString("title");
            float price = product.getInt("price");
            String description = product.getString("description");
            String picture_url = product.getString("picture_url");
            this.anime.setTitle(title);
            this.anime.setPrice(price);
            this.anime.setDescription(description);
            this.anime.setPicture_url(picture_url);
            System.out.println("4");
            productRepo.save(anime);
            System.out.println("5");
            JSONObject message = new JSONObject();
            message.put("type", this.type);
            message.put("requestId", this.requestId);
            message.put("data", this.product);
            message.put("status", "ok");
            message.put("error", "null");
            System.out.println("anime_request_udpate stuurt deze message: " + message);
            productRepo.save(anime);
            kafkaTemplate.send("anime-response", message.toString());
        } catch (Exception e){
            System.out.println("6 laatste");
            JSONObject message = new JSONObject();
            message.put("type", this.type);
            message.put("requestId", this.requestId);
            message.put("param", ob.getJSONObject("param"));
            message.put("status", "nok");
            message.put("error", "null");
            System.out.println("ERROR gevangen!!!! --> nok");
            System.out.println("anime_request_udpate stuurt deze message: " + message);
            kafkaTemplate.send("anime-response", message.toString());
        }

    }

    private void anime_request_post(JSONObject ob) throws JSONException {
        try {
            this.product = ob.getJSONObject("param");
            String title = product.getString("title");
            int price = product.getInt("price");
            String description = product.getString("description");
            String picture_url = product.getString("picture_url");
            this.anime = new Product(title, description, price, picture_url);

            JSONObject message = new JSONObject();
            message.put("type", this.type);
            message.put("requestId", this.requestId);
            message.put("data", this.product);
            message.put("status", "ok");
            message.put("error", "null");
            System.out.println("anime_request_post stuurt deze message: " + message);
            productRepo.save(anime);
            kafkaTemplate.send("anime-response", message.toString());
        } catch (Exception e){
            JSONObject message = new JSONObject();
            message.put("type", this.type);
            message.put("requestId", this.requestId);
            message.put("param", ob.getJSONObject("param"));
            message.put("status", "nok");
            message.put("error", e);
            System.out.println("ERROR gevangen!!!! --> nok");
            System.out.println("anime_request_post stuurt deze message: " + message);
            kafkaTemplate.send("anime-response", message.toString());
        }

    }

    private void anime_request_list(JSONObject ob) throws JSONException {
        try {
            list = productRepo.findAll();
            System.out.println("anime-request_list geeft :" + list);

            //return message in anime-response
            JSONObject message = new JSONObject();
            message.put("type", this.type);
            message.put("requestId", this.requestId);
            message.put("data", list);
            message.put("status", "ok");
            message.put("error", "null");
            System.out.println("anime_request_list stuurt deze message: " + message);
            kafkaTemplate.send("anime-response", message.toString());
        } catch (Exception e){
            JSONObject message = new JSONObject();
            message.put("type", this.type);
            message.put("requestId", this.requestId);
            message.put("status", "nok");
            message.put("error", "e");
            System.out.println("ERROR gevangen!!!! --> nok");
            System.out.println("anime_request_list stuurt deze message: " + message);
            kafkaTemplate.send("anime-response", message.toString());
        }
    }

    //MET ID IN PARAM getrequest
    private void anime_request_get(JSONObject ob) throws JSONException {
        try {
            long id = ob.getInt("param");
            this.anime = productRepo.findById(id).get();
            System.out.println("anime_request_get krijgt request voor: " + anime);
            JSONObject getanime = new JSONObject();
            getanime.put("id", anime.getId());
            getanime.put("title", anime.getTitle());
            getanime.put("price", anime.getPrice());
            getanime.put("description", anime.getDescription());
            getanime.put("pircture_url", anime.getPicture_url());

            //return message in anime-response
            JSONObject message = new JSONObject();
            message.put("type", this.type);
            message.put("requestId", this.requestId);
            message.put("data", getanime);
            message.put("status", "ok");
            message.put("error", "null");
            System.out.println("anime_request_get stuurt deze message : " + message);
            kafkaTemplate.send("anime-response", message.toString());

        } catch (Exception e){
            JSONObject message = new JSONObject();
            message.put("type", this.type);
            message.put("requestId", this.requestId);
            message.put("param", ob.getInt("param"));
            message.put("status", "nok");
            message.put("error", "null");
            System.out.println("ERROR gevangen!!!! --> nok");
            System.out.println("anime_request_get stuurt deze message : " + message);
            kafkaTemplate.send("anime-response", message.toString());
        }
    }
    /*
    public void consumeJson(KafkaAnimeRequest kafkaAnimeRequest) throws JsonProcessingException, JSONException {
        type = kafkaAnimeRequest.getType();
        requestId = kafkaAnimeRequest.getRequestId();
        param = kafkaAnimeRequest.getParam();

        Product product = (Product) productRepo.findById(param).get();

        System.out.println("Consumed Json-message:" + kafkaAnimeRequest);

        for (Product p : productRepo.findAll()) {
            if (p.getTitle().equals(product.getTitle())
                    && p.getDescription().equals(product.getDescription())
                    && p.getPrice() == product.getPrice()
                    && p.getType().equals(product.getType())
                    && p.getPicture_url() == product.getPicture_url()
            ) {
                System.out.println("dit product bestaat al!!");
                gevondenProduct = p;
                sendResponseMessage(true);
            }
        }
        if (gevondenProduct == null) {
            System.out.println("momenteel in de databank:");
            for (Product p : productRepo.findAll()) {
                System.out.println("\n" + p);
            }
            //System.out.println("PRODUCT NIET GEVONDEN!!!");
            //Product jsonProduct = new ObjectMapper().readValue(product.getJsonValue(), Product.class);
            //System.out.println(jsonProduct);
            productRepo.save(product);
            System.out.println("NIEUW PRODUCT OPGESLAAN! =" + product);
            gevondenProduct = product;
            sendResponseMessage(false);
        }
    }
        /*
        ProductOrder productorder = kafkaAnimeRequest.getParam();
        Product product = productorder.getProduct();
        tmpProductId = productorder.getTmpProductId();
        orderId = productorder.getOrderId();
        serviceId = productorder.getServiceId();

        System.out.println("Consumed Json-message:" + kafkaAnimeRequest);

        for(Product p :productRepo.findAll()){
            if (p.getTitle().equals(product.getTitle())
                    && p.getDescription().equals(product.getDescription())
                    &&p.getPrice() == product.getPrice()
                    && p.getType().equals(product.getType())
                    && p.getPicture_url() == product.getPicture_url()
            ){
                System.out.println("dit product bestaat al!!");
                gevondenProduct = p;
                sendResponseMessage(true);
            }}
        if (gevondenProduct == null){
            System.out.println("momenteel in de databank:");
            for(Product p :productRepo.findAll()){
                System.out.println( "\n" + p);}
            //System.out.println("PRODUCT NIET GEVONDEN!!!");
            //Product jsonProduct = new ObjectMapper().readValue(product.getJsonValue(), Product.class);
            //System.out.println(jsonProduct);
            productRepo.save(product);
            System.out.println("NIEUW PRODUCT OPGESLAAN! =" + product);
            gevondenProduct = product;
            sendResponseMessage(false);
        }

         }


        public void sendResponseMessage ( boolean gelukt) throws JSONException {
            //JSONObject tussenResponse = new JSONObject();
            //tussenResponse.put("orderId", orderId);
            //tussenResponse.put("tmpProductId", tmpProductId);
            //tussenResponse.put("serviceId", serviceId);
            if (gelukt) {
                tussenResponse.put("product", gevondenProduct);
                tussenResponse.put("status", "ok");
                tussenResponse.put("realProductId", gevondenProduct.getId());
            } else {
                tussenResponse.put("status", "nok");
            }
            JSONObject response = new JSONObject();
            response.put("type", type);
            response.put("requestId", requestId);
            System.out.println("response naar Anime-response is dus: " + response.toString());
            kafkaTemplate.send("Anime-response", response.toString());
        }
        */
    }

