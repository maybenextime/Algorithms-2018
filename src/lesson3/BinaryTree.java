package lesson3;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// Attention: comparable supported but comparator is not
@SuppressWarnings("WeakerAccess")
public class BinaryTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private static class Node<T> {
        final T value;

        Node<T> left = null;

        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root = null;

    private int size = 0;

    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        } else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        } else {
            assert closest.right == null;
            closest.right = newNode;
        }
        size++;
        return true;
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     * Трудоемкость: T = O(logN)  -N количество узлов в дереве
     * Ресурсоемкость: R = O(1)
     */
    @Override
    public boolean remove(Object o) {
        return remove(root, o);
    }

    public boolean remove(Node<T> node, Object o) {
        T t = (T) o;
        Node<T> closet = find(t);
        if (closet.value != t) return false;

        Node<T> nodeMin;
        if (closet.right != null) {
            nodeMin = minValue(closet.right);
        } else {
            nodeMin = null;
        }
        Node<T> parentOfNodeMin = findParent(node, nodeMin);
        Node<T> parentOfRemoveNode = findParent(node, closet);
        remove2(parentOfRemoveNode, closet, parentOfNodeMin, nodeMin);
        size--;
        return true;
    }

    public void remove2(Node<T> parRemove, Node<T> cur, Node<T> paMin, Node<T> min) {
        if (min == null) {
            if (cur == root) root = root.left;
            else {
                changeNode(parRemove, cur, cur.left);
            }
        } else {
            if (cur.left == null && cur.right == null) {
                changeNode(parRemove, cur, null);
                return;
            }
            if (cur.right == min) {
                paMin.right = min.right;
            } else paMin.left = min.right;

            min.left = cur.left;
            min.right = cur.right;

            if (cur == root) {
                root = min;
            } else {
                changeNode(parRemove, cur, min);
            }
        }
    }


    private void changeNode(Node<T> parN, Node<T> cloN, Node<T> newN) {
        if (parN.value.compareTo(cloN.value) > 0) parN.left = newN;
        else parN.right = newN;
    }

    public Node<T> minValue(Node<T> node) {
        if (node.left == null) return node;
        else return (minValue(node.left));
    }

    public Node<T> findParent(Node<T> node, Node<T> cur) {
        if (root == cur || cur == null) return null;
        Node<T> left = node.left;
        Node<T> right = node.right;
        int comparison = cur.value.compareTo(node.value);
        if (left == null && right != null) {
            if (right == cur) return node;
        }
        if (right == null && left != null)
            if (left == cur) return node;

        if (left != null && right != null)
            if (left == cur || right == cur) {
                return node;
            }
        if (comparison < 0) return findParent(node.left, cur);
        else return findParent(node.right, cur);
    }


    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        } else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        } else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    public class BinaryTreeIterator implements Iterator<T> {

        private Node<T> current = null;
        private Node<T> parentOfCurrent = null;
        private Stack<Node<T>> stack;
        private Stack<Node<T>> parentStack;


        private BinaryTreeIterator() {
            stack = new Stack<>();
            parentStack = new Stack<>();
            pushToStack(stack, root);
            pushToStack(parentStack, root);
            parentStack.pop();
        }

        /**
         * Поиск следующего элемента
         * Средняя
         * Трудоемкость: T = O(1)
         * Ресурсоемкость: R = O(h), h высота деревне
         */

        private Node<T> findNext() {
            Node<T> node = stack.pop();
            if (!parentStack.isEmpty()) parentOfCurrent = parentStack.pop();
            else parentOfCurrent = null;
            if (node.right != null) {
                pushToStack(stack, node.right);
                parentStack.push(node);
                pushToStack(parentStack, node.right);
                if (!parentStack.isEmpty()) parentStack.pop();
            }
            return node;
        }

        private void pushToStack(Stack<Node<T>> pushStack, Node<T> node) {
            while (node != null) {
                pushStack.push(node);
                node = node.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.empty();
        }

        private Node<T> pre = null;

        @Override
        public T next() {
            pre = current;
            current = findNext();
            Node<T> t = parentOfCurrent;
            if (current == null) throw new NoSuchElementException();
            return current.value;
        }

        /**
         * Удаление следующего элемента
         * Сложная
         * Трудоемкость: T = O(1)
         * Ресурсоемкость: R = O(h), h высота деревне
         */
        @Override
        public void remove() {
            Node<T> nodeMin;
            Node<T> parentOfNodeMin;
            if (current.right == null) {
                nodeMin = null;
                parentOfNodeMin = null;
            } else {
                nodeMin = stack.pop();
                stack.push(nodeMin);
                if (current.right == nodeMin) {
                    parentOfNodeMin = current;
                } else {
                    stack.pop();
                    parentOfNodeMin = stack.pop();
                    stack.push(parentOfNodeMin);
                    stack.push(nodeMin);
                }
            }
            remove2(parentOfCurrent, current, parentOfNodeMin, nodeMin);
            current = pre;
            size--;
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinaryTreeIterator();
    }

    @Override
    public int size() {
        return size;
    }


    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     */
    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        // TODO
        throw new NotImplementedError();
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }
}


