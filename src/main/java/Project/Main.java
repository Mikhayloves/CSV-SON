package Project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import static java.lang.ref.Cleaner.create;

public class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        String fileNameJson = "data.json";
        String fileNameJson2 = "data2.json";


        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        String fileJson = writeString(json, fileNameJson);


        List<Employee> listtwo = parseXML("data.xml");
        String jsontwo = listToJson(listtwo);
        String data2 = writeString(jsontwo, fileNameJson2);
    }


    public static void Created(String file) throws IOException {
        File file1 = new File(file);
        file1.createNewFile();
    }

    public static List<Employee> parseCSV(String[] array, String file) {
        List<Employee> arra = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(array);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();

            arra = csv.parse();
            arra.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return arra;
    }

    public static String listToJson(List list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder
                .setPrettyPrinting()
                .create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        System.out.println(gson.toJson(list));
        return json;
    }

    public static String writeString(String json, String filename) {
        try (FileWriter file = new FileWriter(filename)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static List <Employee> parseXML(String string) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> employees = new ArrayList<>();
        try {
            var factory = DocumentBuilderFactory.newInstance();
            var builder = factory.newDocumentBuilder();
            var filePath = new File(string);
            Document document = builder.parse(filePath);
            Node root = document.getDocumentElement();
            read(root);
            System.out.println("Коренвой " + root.getNodeName());
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public static void read(Node node) {
        NodeList nodeList = node.getChildNodes();
        Employee employee = null;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node_ = nodeList.item(i);
            if (Node.ENTITY_NODE == node_.getNodeType()) {
                var element = (Element) node_;
                NamedNodeMap map = element.getAttributes();
                employee = new Employee();
                for (int j = 0; j < map.getLength(); j++) {
                    String attrName = map.item(j).getNodeName();
                    String attrValue = map.item(j).getNodeValue();

                    System.out.println("Атрибут " + attrName + "; значение " + attrValue);
                }
                read(node_);
            }
        }
    }
}

