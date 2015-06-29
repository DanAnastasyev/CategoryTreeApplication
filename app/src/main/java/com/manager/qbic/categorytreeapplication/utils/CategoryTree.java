package com.manager.qbic.categorytreeapplication.utils;

import com.manager.qbic.categorytreeapplication.holder.IconTreeItemHolder;
import com.unnamed.b.atv.com.qbic.R;
import com.unnamed.b.atv.model.TreeNode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class CategoryTree {
    private static class Node extends TreeNode {
        int id;
        String name;
        int parentId;
        ArrayList<Integer> childIds = new ArrayList<>();
        Node parent = null;
        ArrayList<Node> childs = new ArrayList<>();

        public Node(JSONObject jsonObject) throws JSONException {
            super(new IconTreeItemHolder.IconTreeItem(R.string.ic_drive_file,
                    jsonObject.getString("localizedName")));

            id = jsonObject.getInt("categoryId");
            name = jsonObject.getString("localizedName");
            parentId = jsonObject.getInt("parentId");
            if (jsonObject.has("childCategories")) {
                JSONArray jsonArray = jsonObject.getJSONArray("childCategories");
                for (int i = 0; i < jsonArray.length(); ++i) {
                    childIds.add(jsonArray.getInt(i));
                }
            }
        }
    }

    // Корни различных компонент связности
    // По идее, после полного построения дерева здесь останется одна вершина
    private ArrayList<Node> roots = new ArrayList<>();

    private void addNode(Node newNode) {
        // Если это первая добавляемая вершина, делаем её новым корнем и выходим
        if (roots.isEmpty()) {
            roots.add(newNode);
            return;
        }
        // Проверяем, не является ли новая вершина родителем какого-нибудь из корней уже построенных деревьев
        for (Node root : roots) {
            if (root.parentId == newNode.id) {
                // Если является, делаем её новым корнем
                newNode.childs.add(root);
                newNode.addChild(root);
            }
        }
        // Удаляем вершины, не являющиеся больше корнями
        if (!newNode.childs.isEmpty()) {
            roots.add(newNode);
            for (Node child : newNode.childs) {
                roots.remove(child);
            }
        }

        // Ищем родителя для новой вершины поиском в ширину
        LinkedList<Node> queue = new LinkedList<>();
        for (Node root : roots) {
            queue.add(root);
        }
        while (!queue.isEmpty()) {
            Node currNode = queue.pollFirst();
            if (currNode.id == newNode.parentId) {
                newNode.parent = currNode;
                currNode.childs.add(newNode);
                currNode.addChild(newNode);
                // Теперь новая вершина уже не корень
                if (roots.contains(newNode)) {
                    roots.remove(newNode);
                }
                return;
            }
            for (Node node : currNode.childs) {
                queue.add(node);
            }
        }
        // Если попали сюда, значит, поиск в глубину не нашел родителя, делаем новую вершину корнем
        if (!roots.contains(newNode)) {
            roots.add(newNode);
        }
    }

    public CategoryTree(JSONObject jsonObject) throws JSONException {
        JSONObject result = jsonObject.getJSONObject("result");
        Iterator<String> iterator = result.keys();
        while (iterator.hasNext()) {
            addNode(new Node(result.getJSONObject(iterator.next())));
        }
    }

    public ArrayList<Node> getRoot() {
        return roots;
    }
}