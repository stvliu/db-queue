package ru.yandex.money.common.dbqueue.settings;

/**
 * Стратегия выполнения задачи в очереди.
 *
 * @author Oleg Kandaurov
 * @since 16.07.2017
 */
public enum ProcessingMode {
    /**
     * Задача будет успешно обработана минимум один раз.
     * Каждое обращение к БД производится в отдельной транзакции.
     * <p>
     * Следует применять при наличии внешних вызовов в обработчике.
     * Однако внешний вызов должен быть реентрабельным.
     * В подобном подходе есть вероятность, что после успешной обработки клиентского кода
     * невозможно будет удалить задачу из очереди.
     * Соответственно клиент успешно обработает задачу повторно.
     */
    SEPARATE_TRANSACTIONS,
    /**
     * Выполнение задачи обернуто транзакцию.
     * Задача будет успешно обрабатана ровно один раз при соблюдении условий.
     * <p>
     * Применять следует только если в обработчике нет внешних вызовов
     * и обработка очереди заключается только к обращениям к той же БД, где хранятся очереди.
     * При соблюдении условий гарантируется, что задача успешно обработается только единожды.
     * При наличии внешних вызовов не рекомендуется использовать поскольку транзакция
     * может быть открыта на долгое время и исчерпается их пул.
     */
    WRAP_IN_TRANSACTION,

    /**
     * Задача будет успешно обработана минимум один раз, асинхронно, в заданном обработчике
     * {@link ru.yandex.money.common.dbqueue.api.QueueExternalExecutor}.
     * Каждое обращение к БД производится в отдельной транзакции.
     * <p>
     * Данный режим требует дополнительной конфигурации и управления внешним обработчиком.
     * Плюсом этого режима является то, что обеспечивается бОльшая пропускная способность
     * и можно точно задать верхнюю границу скорости разбора задач.
     * Достигается это тем, что в потоках очереди производится только выборка задач,
     * Последующая обработка производится асинхронно в отдельном обработчике.
     * <p>
     * Данный режим следует применять когда очередь выполняет долгие операции.
     * Если не использовать данный режим, то увеличение количества потоков очереди решит проблему,
     * однако это также приведет к увеличению количества холостых опросов БД на выборку задач.
     */
    USE_EXTERNAL_EXECUTOR
}
