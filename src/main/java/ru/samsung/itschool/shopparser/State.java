package ru.samsung.itschool.shopparser;

/**
 * Данный enum представляет собой список всех состояний конечного автомата для разбора строк вида
 *
 * Ivan Ivanov | 359 "apples", 90 "coffee", 30 "legumes (peas, beans, peanuts)".
 *
 * и вывода частей таких строк в виде отдельных строк, разделенных переносом строки. Каждое
 * состояние имеет свою реализацию метода process для генерации выхода и вычисления нового
 * состояния.
 *
 * Также, данный enum содержит метод main, который содержит демонстрацию использования
 * данного конечного автомата на константной строке.
 *
 * @author - Pavel K {@literal <theservy@gmail.com>}
 */
public enum State {
    /**
     * Состояние, соответствующее чтению имени покупателя - начальное состояние
     */
    READING_NAME {
        @Override
        public State process(char currentChar, StringBuilder output) {
            if (currentChar == '|') { // если мы нашли разделитель
                output.append("\n");  // мы закончили с именем - осуществим перевод на новую строку
                return READING_COST;  // и переключаемся в состояние "чтения стоимости"
            }
            output.append(currentChar); // если же текущий символ - не разделитель - просто добавляем его на выход
            return READING_NAME;        // и остаемся в текущем состоянии
        }
    },

    /**
     * Состояние, соответствующее чтению стоимости покупки
     */
    READING_COST {
        @Override
        public State process(char currentChar, StringBuilder output) {
            if (currentChar == '"') {   // если мы нашли разделитель (кавычку)
                output.append("\n");    // мы закончили со стоимостью, осуществим переход на новую строку
                return READING_PRODUCT; // и перейдем в состояние "чтения названия товара"
            }
            output.append(currentChar); // если же текущий символ - не разделитель - просто добавляем его на выход
            return READING_COST;        // и остаемся в текущем состоянии
        }
    },

    /**
     * Состояние, соответствующее чтению названия товара
     */
    READING_PRODUCT {
        @Override
        public State process(char currentChar, StringBuilder output) {
            if (currentChar == '"') {     // если мы нашли разделитель (кавычку)
                output.append("\n");      // мы закончили разбирать название товара, осуществим переход на новую строку
                return READING_DELIMITER; // и перейдем в состояние "чтения разделителя" (запятой или точки)
            }
            output.append(currentChar);   // если же текущий символ - не разделитель - просто добавляем его на выход
            return READING_PRODUCT;       // и остаемся в текущем состоянии
        }
    },

    /**
     * Состояние, соответствующее чтению разделителя между элементами списка (запятой) или
     * признака окончания списка (точкой).
     */
    READING_DELIMITER {
        @Override
        public State process(char currentChar, StringBuilder output) {
            if (currentChar == '.')      // если мы нашли разделитель: точку
                return FINISHED;         // наш конечный автомат закончил свою работу - переходим в состояние завершения
            if (currentChar == ',')      // если же мы нашли разделитель: запятую
                return READING_COST;     // значит у нас есть еще один элемент в списке - переходим в
                                         // состояние чтения стоимости

            if (currentChar != ' ')      // если текущий символ не запятая и не точка, единственный допустимый вариант - пробел
                throw new IllegalStateException(String.format("Ожидаю разделитель - точку, запятую или пробел, но нашел символ %c. " +
                        "Строка не соответствует формату.", currentChar));

            // текущий символ - пробел, остаемся в текущем состоянии
            return READING_DELIMITER;
        }
    },

    /**
     * Состояние, соответствующее окончанию работы конечного автомата
     */
    FINISHED {
        @Override
        public State process(char currentChar, StringBuilder output) {
            // проверяем, что текущий символ - это пробел или перевод строки, только они допустимы после заключительной точки
            if ((currentChar != ' ') && (currentChar != '\n') && (currentChar != '\r'))
                throw new IllegalStateException("После конечной точки допустимы только пробелы и символ конца строки");

            // остаемся в текущем состоянии
            return  FINISHED;
        }
    };

    /**
     * Возвращает новое состояние конечного автомата на основании
     * текущего входного символа currentChar и пишет необходимые
     * выходные данные в output.
     *
     * @param currentChar текущий символ для обработки
     * @param output буфер, в который добавляется выход конечного автомата
     * @return новое состояние конечного автомата
     */
    public abstract State process(char currentChar, StringBuilder output);

    /**
     * Реализация основного метода программы для демонстрации работы конечного автомата
     * @param args параметры командной строки; данная реализация их игнорирует
     */
    public static void main(String[] args) {
        // тестовая входная строка
        String input = "Ivan Ivanov | 359 \"apples\", 90 \"coffee\", 30 \"legumes (peas, beans, peanuts)\".";

        // сюда мы будем собирать результат работы конечного автомата
        StringBuilder output = new StringBuilder();

        // текущее состояние конечного автомата: изначально это чтение имени покупателя
        State currentState = State.READING_NAME;
        for (char c: input.toCharArray()) { // проходим по всем символам строки
            // вычисляем новое состояние конечного автомата на основании текущего состояния; передаем в метод process
            // текущий обрабатываемый символ c и output, куда будет добавляется результат
            currentState = currentState.process(c, output);
        }

        // после окончания цикла мы ожидаем, что конечный автомат будет в состоянии FINISHED
        if (currentState != State.FINISHED)
            throw new IllegalStateException("По окончании парсинга строки мы не оказались в финальном состоянии. Строка не соответствует формату.");

        // выведем результат
        System.out.print(output);
    }
}
