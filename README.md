# Сервис медицинских показаний

В классе Main, создаются несколько тестовых данных о пациентах и записываются в файл (репозиторий)

##### Редактирование класса MedicalServiceImpl

~~~ java
    public void checkTemperature(String patientId, BigDecimal temperature) {
        PatientInfo patientInfo = getPatientInfo(patientId);
        if (patientInfo.getHealthInfo().getNormalTemperature().subtract(new BigDecimal("1.5")).compareTo(temperature) > 0) {
            String message = String.format("Warning, patient with id: %s, need help", patientInfo.getId());
            System.out.printf("Warning, patient with id: %s, need help", patientInfo.getId());
            alertService.send(message);
        }
    }
~~~
метод subtract(x) заменен на add(x). Вероятно была ошибка. Нужно выводить сообщение если измеряемая температура превышает обычную температуру больше чем на 1,5 градуса
~~~ java
public void checkTemperature(String patientId, BigDecimal temperature) {
        PatientInfo patientInfo = getPatientInfo(patientId);
        if (patientInfo.getHealthInfo().getNormalTemperature().add(new BigDecimal("1.5")).compareTo(temperature) > 0) {
            String message = String.format("Warning, patient with id: %s, need help", patientInfo.getId());
            System.out.printf("Warning, patient with id: %s, need help", patientInfo.getId());
            alertService.send(message);
        }
    }
~~~

##### Тестирование MedicalServiceImpl
~~~ text
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running ru.netology.patient.service.medical.MedicalServiceImplTest
Тестирование класса MedicalServiceImpl...
Проверка вывода сообщения во время проверки температуры
Warning, patient with id: 123, need helpWarning, patient with id: 123, need help
Проверка вывода сообщения во время проверки давления
Warning, patient with id: 123, need help
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.904 s - in ru.netology.patient.service.medical.MedicalServiceImplTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
~~~