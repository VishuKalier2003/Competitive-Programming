package SegmentTree;

class MyCalendarThree {

    public class Node {
        public Node left, right;
        public int rangeLeft, rangeRight, bookingCount, lazy;

        public Node(int left, int right) {this.rangeLeft = left; this.rangeRight = right; this.bookingCount = 0; this.lazy = 0;}

        public void updateBooking() {this.bookingCount++;}
        public void updateLazy() {this.lazy++;}
    }

    public Node root;
    public final int start = 0, end = 1_000_000_000;
    public int kBooking;

    public MyCalendarThree() {
        root = new Node(start, end);
        kBooking = 0;
    }

    public int book(int startTime, int endTime) {
        updateQuery(root, start, end, startTime, endTime-1);
        kBooking = Math.max(kBooking, rangeMaxQuery(root, start, end, startTime, endTime-1));
        return kBooking;
    }

    public Node updateQuery(Node root,
                        int rangeLeft, int rangeRight,
                        int queryLeft, int queryRight) {
    // 1) NO OVERLAP => nothing to do, don’t even build a node
    if (queryRight < rangeLeft || queryLeft > rangeRight)
        return root;

    // 2) If we need to touch this segment, ensure the node exists
    if (root == null)
        root = new Node(rangeLeft, rangeRight);

    // 3) FULL OVERLAP => increment bookingCount and lazy
    if (queryLeft <= rangeLeft && rangeRight <= queryRight) {
        root.bookingCount++;
        root.lazy++;
        return root;
    }

    // 4) PARTIAL OVERLAP => push down pending lazy, recurse, then pull up
    lazyPropagation(root, rangeLeft, rangeRight);
    int mid = rangeLeft + ((rangeRight - rangeLeft) >> 1);
    root.left  = updateQuery(root.left,  rangeLeft, mid,        queryLeft, queryRight);
    root.right = updateQuery(root.right, mid + 1, rangeRight,  queryLeft, queryRight);

    // pull up the max from children
    int leftMax  = (root.left  != null ? root.left.bookingCount  : 0);
    int rightMax = (root.right != null ? root.right.bookingCount : 0);
    root.bookingCount = Math.max(leftMax, rightMax);

    return root;
}


    public int rangeMaxQuery(Node root, int rangeLeft, int rangeRight, int queryLeft,  int queryRight) {
    // 1) No node or no overlap => zero
    if (root == null || queryRight < rangeLeft || queryLeft > rangeRight)
        return 0;

    // 2) Full overlap => just return stored value
    if (queryLeft <= rangeLeft && rangeRight <= queryRight)
        return root.bookingCount;

    // 3) Otherwise, push down, recurse, return max
    lazyPropagation(root, rangeLeft, rangeRight);
    int mid = rangeLeft + ((rangeRight - rangeLeft) >> 1);
    int leftMax  = rangeMaxQuery(root.left,  rangeLeft, mid,         queryLeft, queryRight);
    int rightMax = rangeMaxQuery(root.right, mid + 1, rangeRight,   queryLeft, queryRight);
    return Math.max(leftMax, rightMax);
}


    public void lazyPropagation(Node root, int rangeLeft, int rangeRight) {
        if(root.lazy == 0)
            return;
        int mid = rangeLeft + ((rangeRight - rangeLeft) >> 1);
        if(root.left == null)
            root.left = new Node(rangeLeft, mid);
        if(root.right == null)
            root.right = new Node(mid+1, rangeRight);
        int update = root.lazy;
        root.left.bookingCount += update;
        root.right.bookingCount += update;
        root.left.lazy += update;
        root.right.lazy += update;
        root.lazy = 0;
        return;
    }
}
