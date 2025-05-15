from manim import *

class BinaryLiftingVisualization(Scene):
    def construct(self):
        # Define tree edges and structure
        edges = [(1, 2), (1, 3), (2, 4), (2, 5)]
        positions = {
            1: UP * 2,
            2: LEFT + UP,
            3: RIGHT + UP,
            4: LEFT * 2,
            5: LEFT * 1.5 + DOWN
        }

        # Draw nodes and labels
        nodes = {}
        labels = {}
        for node, pos in positions.items():
            circle = Circle(radius=0.4).move_to(pos).set_fill(WHITE, opacity=1).set_stroke(BLACK, 2)
            label = Text(str(node), font_size=24).move_to(pos)
            self.add(circle, label)
            nodes[node] = circle
            labels[node] = label

        # Draw edges
        for u, v in edges:
            self.add(Line(positions[u], positions[v]))

        # Build adjacency list
        adj = {i: [] for i in positions}
        for u, v in edges:
            adj[u].append(v)
            adj[v].append(u)

        # Parameters
        n = len(positions)
        LOG = 3
        lift_table = [[-1 for _ in range(n + 1)] for _ in range(LOG)]
        depth = [-1 for _ in range(n + 1)]

        # Table display
        table_origin = DOWN * 2 + LEFT * 4
        table = VGroup()
        headers = VGroup(Text("k \\ v", font_size=28).move_to(table_origin + RIGHT * 0.5 + UP * 0.5))
        for v in range(1, n + 1):
            headers.add(Text(str(v), font_size=28).move_to(table_origin + RIGHT * (v + 0.5)))
        for k in range(LOG):
            row = VGroup(Text(str(k), font_size=28).move_to(table_origin + DOWN * (k + 1) + RIGHT * 0.5))
            for v in range(1, n + 1):
                text = Text("-", font_size=24).move_to(table_origin + DOWN * (k + 1) + RIGHT * (v + 0.5))
                row.add(text)
            table.add(row)
        table.add(headers)
        self.add(table)

        # DFS for binary lifting
        def dfs(node, parent):
            depth[node] = depth[parent] + 1 if parent != -1 else 0
            lift_table[0][node] = parent

            self.play(nodes[node].animate.set_fill(YELLOW), run_time=0.5)
            if parent != -1:
                self.update_table(0, node, parent, table)
            self.wait(0.2)

            # Fill higher ancestors
            for k in range(1, LOG):
                prev = lift_table[k - 1][node]
                if prev != -1:
                    lift_table[k][node] = lift_table[k - 1][prev]
                    self.update_table(k, node, lift_table[k][node], table)
                    self.wait(0.2)

            for nei in adj[node]:
                if nei != parent:
                    dfs(nei, node)
            self.play(nodes[node].animate.set_fill(WHITE), run_time=0.5)

        dfs(1, -1)
        self.wait(2)

    def update_table(self, k, v, val, table):
        # Note: table[0] is the first LOG row, table[-1] is headers
        target = table[0][v]
        new_val = Text(str(val), font_size=24).move_to(target.get_center())
        self.play(Transform(target, new_val), run_time=0.3)
