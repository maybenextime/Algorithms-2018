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
     */
    @Override
    public boolean remove(Object o) {
        if (!contains(o)) return false;
        else
            return remove(root, o);
    }

    public boolean remove(Node<T> node, Object o) {
        T t = (T) o;
        Node<T> closet = find(t);
        T minValue;
        if (closet.right != null) minValue = minValue(closet.right);
        else minValue = null;
        if (minValue == null) {
            if (closet == root) root = root.left;
            else {
                Node<T> parentOfRemoveNode = findParent(node, t);
                changeNode(parentOfRemoveNode, closet, closet.left);
            }
        } else {
            Node<T> parentOfNodeMin = findParent(node, minValue);
            Node<T> newNode = find(minValue);
            Node<T> parentOfRemoveNode = findParent(node, t);

            if (closet.left == null && closet.right == null) {
                changeNode(parentOfRemoveNode, closet, null);
                return true;
            }

            if (closet.right == newNode) {
                parentOfNodeMin.right = newNode.right;
            } else parentOfNodeMin.left = newNode.right;

            newNode.left = closet.left;
            newNode.right = closet.right;

            if (closet == root) {
                root = newNode;
            } else {
                changeNode(parentOfRemoveNode, closet, newNode);
            }
        }
        size--;
        return true;

    }

    private void changeNode(Node<T> parN, Node<T> cloN, Node<T> newN) {
        if (parN.value.compareTo(cloN.value) > 0) parN.left = newN;
        else parN.right = newN;
    }

    public T minValue(Node<T> node) {
        if (node.left == null) return node.value;
        else return (minValue(node.left));
    }

    public Node<T> findParent(Node<T> node, T value1) {
        if (root.value == value1) return null;
        Node<T> left = node.left;
        Node<T> right = node.right;
        int comparison = value1.compareTo(node.value);
        if (left == null && right != null) {
            if (right.value == value1) return node;
        }
        if (right == null && left != null)
            if (left.value == value1) return node;

        if (left != null && right != null)
            if (left.value == value1 || right.value == value1) {
                return node;
            }
        if (comparison < 0) return findParent(node.left, value1);
        else return findParent(node.right, value1);

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

        private BinaryTreeIterator() {
        }

        /**
         * Поиск следующего элемента
         * Средняя
         */
        private Node<T> bigParent(Node<T> node) {
            if (node == root) return null;
            Node<T> pNode = findParent(root, node.value);
            if (pNode.right == node) return bigParent(pNode);
            else return pNode;
        }

        private Node<T> findNext() {
            if (current == null) {
                return find(minValue(root));
            }
            if (current == root) {
                if (current.right == null) return null;
                else return find(minValue(root.right));
            } else {
                if (current.right == null) return bigParent(current);
                else return find(minValue(current.right));
            }

        }

        @Override
        public boolean hasNext() {
            return findNext() != null;
        }

        private Node<T> pre = null;

        @Override
        public T next() {
            pre = current;
            current = findNext();
            if (current == null) throw new NoSuchElementException();
            return current.value;
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        @Override
        public void remove() {
            if (current == null) return;
            BinaryTree.this.remove(current.value);
            current = pre;
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
        SortedSet<T> result = new TreeSet<>();
        Iterator iterator = BinaryTree.this.iterator();
        T element;
        while (iterator.hasNext()) {
            element = (T) iterator.next();
            if (element.compareTo(fromElement) >= 0) result.add(element);
            if (element.compareTo(toElement) < 0) break;
        }
        return result;
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

