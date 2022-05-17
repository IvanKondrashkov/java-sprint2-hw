# task-tracker
## Типы задач
Простейшим кирпичиком такой системы является задача (англ. task). У задачи есть следующие свойства:

1. Название, кратко описывающее суть задачи (например, «Переезд»).
2. Описание, в котором раскрываются детали.
3. Уникальный идентификационный номер задачи, по которому её можно будет найти.
4. Статус, отображающий её прогресс. Мы будем выделять следующие этапы жизни задачи:
   * NEW — задача только создана, но к её выполнению ещё не приступили.
   * IN_PROGRESS — над задачей ведётся работа.
   * DONE — задача выполнена.
    
Иногда для выполнения какой-нибудь масштабной задачи её лучше разбить на подзадачи (англ. subtask). Большую задачу, которая делится на подзадачи, мы будем называть эпиком (англ. epic).
Таким образом, в нашей системе задачи могут быть трёх типов: обычные задачи, эпики и подзадачи. Для них должны выполняться следующие условия:
  * Для каждой подзадачи известно, в рамках какого эпика она выполняется.
  * Каждый эпик знает, какие подзадачи в него входят.
  * Завершение всех подзадач эпика считается завершением эпика.

## Менеджер
Кроме классов для описания задач, вам нужно реализовать класс для объекта-менеджера. Он будет запускаться на старте программы и управлять всеми задачами. В нём должны быть реализованы следующие функции:

1. Возможность хранить задачи всех типов. Для этого вам нужно выбрать подходящую коллекцию.
2. Методы для каждого из типа задач(Задача/Эпик/Подзадача):
   * Получение списка всех задач.
   * Удаление всех задач.
   * Получение по идентификатору.
   * Создание. Сам объект должен передаваться в качестве параметра.
   * Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра.
   * Удаление по идентификатору.
3. Дополнительные методы:
   * Получение списка всех подзадач определённого эпика.
4. Управление статусами осуществляется по следующему правилу:
   * Менеджер сам не выбирает статус для задачи. Информация о нём приходит менеджеру вместе с информацией о самой задаче.
   По этим данным в одних случаях он будет сохранять статус, в других будет рассчитывать.
   * Для эпиков: 
     * если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
     * если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
     * во всех остальных случаях статус должен быть IN_PROGRESS.

## Менеджер теперь интерфейс
   * Класс TaskManager
   должен стать интерфейсом. В нём нужно собрать список методов, которые должны быть у любого объекта-менеджера. Вспомогательные методы, если вы их создавали, переносить в интерфейс не нужно.
   * Созданный ранее класс менеджера нужно переименовать в InMemoryTaskManager
   . Именно то, что менеджер хранит всю информацию в оперативной памяти, и есть его главное свойство, позволяющее эффективно управлять задачами. Внутри класса должна остаться реализация методов. При этом важно не забыть имплементировать TaskManager — в Java класс должен явно заявить, что он подходит под требования интерфейса.

## История просмотров задач
Добавьте в программу новую функциональность — нужно, чтобы трекер отображал последние просмотренные пользователем задачи. Для этого реализуйте метод getHistory() — он должен возвращать последние 10 просмотренных задач. Просмотром будем считаться вызов у менеджера методов получения задачи по идентификатору — getTask(), getSubtask() и getEpic(). От повторных просмотров избавляться не нужно.

## Дальнейшая разработка алгоритма с CustomLinkedList и HashMap
Программа должна запоминать порядок вызовов метода add, ведь именно в этом порядке просмотры будут выстраиваться в истории. Для хранения порядка вызовов удобно использовать список.
Если какая-либо задача просматривалась несколько раз, в истории должен отобразиться только последний просмотр. Предыдущий просмотр должен быть удалён сразу же после появления нового — за O(1). Из темы о списках вы узнали, что константное время выполнения операции может гарантировать связный список CustomLinkedList. Однако его стандартная реализация в данном случае не подойдёт. Поэтому вам предстоит написать собственную.

## Про CustomLinkedList
Сначала напишите свою реализацию двусвязного списка задач с методами linkLast и getTasks. linkLast будет добавлять задачу в конец этого списка, а getTasks собирать все задачи из него в обычный ArrayList. Убедитесь, что решение работает. Отдельный класс для списка создавать не нужно — реализуйте его прямо в классе InMemoryHistoryManager. А вот отдельный класс Node для узла списка необходимо добавить.

## Про метод removeNode
Добавьте метод removeNode в класс. В качестве параметра этот метод должен принимать объект Node — узел связного списка и вырезать его.

## Про HashMap
Создайте HashMap — будет достаточно её стандартной реализации. В ключах будут храниться id задач, а в значениях — узлы связного списка. Изначально HashMap пустая. Она будет заполняться по мере добавления новых задач. Напишите реализацию метода add(Task task). Теперь с помощью HashMap и метода удаления removeNode метод add(Task task) будет быстро удалять задачу из списка, если она там есть, а затем вставлять её в конец двусвязного списка. После добавления задачи не забудьте обновить значение узла в HashMap.

## Вторая реализация менеджера
Итак, создайте класс FileBackedTasksManager. В нём вы будете прописывать логику автосохранения в файл. Этот класс, как и InMemoryTasksManager, должен имплементировать интерфейс менеджера TasksManager.

## Метод автосохранения
Пусть новый менеджер получает файл для автосохранения в своём конструкторе и сохраняет его в поле. Создайте метод save без параметров — он будет сохранять текущее состояние менеджера в указанный файл.

Затем нужно продумать логику метода save. Он должен сохранять, все задачи, подзадачи, эпики и историю просмотра любых задач. Для удобства работы рекомендуем выбрать текстовый формат CSV (англ. Comma-Separated Values, «значения, разделённые запятыми»).
```id,type,name,status,description,epic
   1,TASK,Task1,NEW,Description task1,
   2,EPIC,Epic2,DONE,Description epic2,
   3,SUBTASK,Sub Task2,DONE,Description sub task3,2
   
   2,3
```
## Как сохранять задачи в файл и считывать их из него
   * Создайте enum с типами задач.
   * Напишите метод сохранения задачи в строку String toString(Task task) или переопределите базовый.
   * Напишите метод создания задачи из строки Task fromString(String value).
   * Напишите статические методы static String toString(HistoryManager manager) и static List<Integer> fromString(String value) для сохранения и восстановления менеджера истории из CSV.
