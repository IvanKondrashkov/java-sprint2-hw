package ru.yandex.praktikum.manager;

import java.util.*;
import lombok.Getter;
import ru.yandex.praktikum.entity.Task;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList history = new CustomLinkedList();

    @Override
    public void addTask(Task task) {
        if (Objects.isNull(task)) {
            return;
        }

        history.add(task);

        if (history.size > 10) {
            Node node = history.head;
            task = node.data;
            remove(task.getId());
        }
    }

    @Override
    public void remove(long id) {
        Map<Long, Node> nodes = history.getNodes();

        if (nodes.containsKey(id)) {
            Node node = nodes.get(id);
            history.removeNode(node);
            nodes.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }

    @Getter
    private static class CustomLinkedList {
        private Node head;
        private Node tail;
        private int size = 0;
        private final Map<Long, Node> nodes;

        public CustomLinkedList() {
            this.nodes = new HashMap<>();
        }

        public Node linkLast(Task task) {
            final Node oldTail = tail;
            final Node newNode = new Node(task, oldTail, null);
            tail = newNode;

            if (Objects.isNull(oldTail)) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
            size++;
            return newNode;
        }

        public void add(Task task) {
            if (!nodes.containsKey(task.getId())) {
                Node node = linkLast(task);
                nodes.put(task.getId(), node);
            }
        }

        public void removeNode(Node node) {
            final Node next = node.next;
            final Node prev = node.prev;

            if (Objects.isNull(prev)) {
                head = next;
            } else {
                prev.next = next;
                node.prev = null;
            }

            if (Objects.isNull(next)) {
                tail = prev;
            } else {
                next.prev = prev;
                node.next = null;
            }
            node.data = null;
            size--;
        }

        public List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            Node node = head;

            while (Objects.nonNull(node)) {
                Task task = node.data;
                tasks.add(task);
                node = node.next;
            }
            return tasks;
        }

        public int size() {
            return this.size;
        }
    }

    private static class Node {
        private Task data;
        private Node prev;
        private Node next;

        public Node(Task data, Node prev, Node next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }
}
