import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        // Парсинг CSV в список сотрудников
        List<Employee> list = parseCSV(columnMapping, fileName);

        // Проверка на null
        if (list == null) {
            System.out.println("Ошибка при чтении CSV файла");
            return;
        }

        // Преобразование списка в JSON строку
        String json = listToJson(list);

        // Запись JSON строки в файл
        writeString(json, "data.json");

        System.out.println("Конвертация завершена успешно");
    }

    // Объявляем класс Employee как статический
    public static class Employee {
        public long id;
        public String firstName;
        public String lastName;
        public String country;
        public int age;

        public Employee() {
            // Пустой конструктор
        }

        public Employee(long id, String firstName, String lastName, String country, int age) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.country = country;
            this.age = age;
        }
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        try (FileReader reader = new FileReader(fileName)) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true) // Добавлено
                    .build();

            return csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static <T> String listToJson(List<T> list) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type listType = new TypeToken<List<T>>() {
        }.getType();
        return gson.toJson(list, listType);
    }

    private static void writeString(String json, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) { // Используем BufferedWriter
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}