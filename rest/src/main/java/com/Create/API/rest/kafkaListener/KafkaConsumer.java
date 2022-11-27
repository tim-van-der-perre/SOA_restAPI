package com.Create.API.rest.kafkaListener;

import com.Create.API.rest.Models.Product;
import com.Create.API.rest.Models.ProductOrder;
import com.Create.API.rest.Repo.ProductRepo;
import com.Create.API.rest.kafkaConfig.KafkaProducerConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaConsumer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    KafkaProducerConfig kafkaProducerConfig = new KafkaProducerConfig();

    long tmpProductId;
    long orderId;
    long serviceId;
    Product gevondenProduct;

    @Autowired
    public ProductRepo productRepo;

    @KafkaListener(topics = "kafka_example")
    public void consume(String message){System.out.println("Consumed message:" + message);}

    @KafkaListener(topics = "anime", containerFactory = "productKafkaListenerFactory")

    public void consumeJson(ProductOrder productorder) throws JsonProcessingException, JSONException {
        Product product = productorder.getProduct();
        tmpProductId = productorder.getTmpProductId();
        orderId = productorder.getOrderId();
        serviceId = productorder.getServiceId();

        System.out.println("Consumed Json-message:" + productorder);

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
    public void sendResponseMessage(boolean gelukt) throws JSONException {
        JSONObject response = new JSONObject();
        response.put("orderId", orderId);
        response.put("tmpProductId", tmpProductId);
        response.put("serviceId", serviceId);
        if (gelukt){
            response.put("product", gevondenProduct);
            response.put("status", "ok");
            response.put("realProductId", gevondenProduct.getId());
        } else {
            response.put("status", "nok");
        }
        System.out.println("response naar product confirmations is dus: " + response.toString());
        kafkaTemplate.send("product-confirmations", response.toString());
    }
}
