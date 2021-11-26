package demo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Documented;
import java.util.List;
import java.util.Map;

public class Format {

    public static void writeCSV(List<Map<String, Object>> tasks, String fileName) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(new File(fileName + ".csv"));
        StringBuilder csv = new StringBuilder();
        for (Map<String, Object> task : tasks) {
            for (Map.Entry<String, Object> elem : task.entrySet()) {
                csv.append(elem.getValue());
                csv.append(",");
            }
            csv.append("\n");
        }
        printWriter.write(csv.toString());
        printWriter.close();
    }

    public static void writeXML(List<Map<String, Object>> taskModels, String fileName) throws TransformerException, ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("tasks");
        document.appendChild(root);
        for (Map<String, Object> taskModel : taskModels) {
            Element task = document.createElement("task");
            for (Map.Entry<String, Object> elem : taskModel.entrySet()) {
                Element node = document.createElement(elem.getKey());
                node.appendChild(document.createTextNode(elem.getValue().toString()));
                task.appendChild(node);
            }
            root.appendChild(task);
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(fileName + ".xml"));
        transformer.transform(domSource, streamResult);
    }
}
