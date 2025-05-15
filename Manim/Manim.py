from manim import *

class SieveOfEratosthenesLineWithLegend(Scene):
    def construct(self):
        n = 15
        # Title
        title = Text("Sieve of Eratosthenes", font_size=36).to_edge(UP)
        self.play(Write(title))

        # Legend: Prime vs Non-prime
        legend_prime = Square(0.5).set_fill(GREEN, opacity=0.6).set_stroke(WHITE)
        legend_prime_label = Text("Prime", font_size=20).next_to(legend_prime, RIGHT, buff=0.1)
        legend_non = Square(0.5).set_fill(RED, opacity=0.6).set_stroke(WHITE)
        legend_non_label = Text("Non-prime", font_size=20).next_to(legend_non, RIGHT, buff=0.1)
        legend = VGroup(legend_prime, legend_prime_label, legend_non, legend_non_label)
        legend.arrange(RIGHT, buff=0.5).to_corner(UR)
        self.play(FadeIn(legend))

        # Explanation label
        explanation = Text("Initialize all numbers as unmarked.", font_size=24)
        explanation.next_to(title, DOWN, buff=0.5)
        self.play(Write(explanation))

        # Create number boxes
        number_mobs = []
        for i in range(n + 1):
            box = Square(side_length=0.8).set_stroke(WHITE)
            box.set_fill(WHITE, opacity=0)
            label = Text(str(i), font_size=24).move_to(box.get_center())
            group = VGroup(box, label)
            number_mobs.append((i, box, label))

        line = VGroup(*[grp for (_, box, label) in number_mobs for grp in [VGroup(box, label)]])
        line.arrange(RIGHT, buff=0.1).move_to(ORIGIN)
        self.play(FadeIn(line))

        # Pointer
        pointer = Arrow(UP, DOWN, buff=0.05).scale(0.4)
        self.add(pointer)

        # Helpers
        def move_pointer_to(box):
            self.play(pointer.animate.next_to(box, UP, buff=0.05), run_time=0.3)

        def mark_nonprime(box, label):
            self.play(box.animate.set_fill(RED, opacity=0.6), label.animate.set_color(RED), run_time=0.3)

        def mark_prime(box, label):
            self.play(box.animate.set_fill(GREEN, opacity=0.6), label.animate.set_color(GREEN), run_time=0.3)

        # Mark 0 and 1
        update_text = lambda txt: self.play(Transform(explanation, Text(txt, font_size=24).next_to(title, DOWN, buff=0.5)))
        update_text("Mark 0 and 1 as non-prime.")
        for idx in (0, 1):
            _, box, label = number_mobs[idx]
            move_pointer_to(box)
            mark_nonprime(box, label)

        # Sieve
        update_text("Iterate i from 2 to sqrt(n).")
        for i in range(2, int(n**0.5) + 1):
            num_i, box_i, label_i = number_mobs[i]
            if box_i.get_fill_opacity() == 0:
                update_text(f"Found prime: {i}. Marking multiples.")
                mark_prime(box_i, label_i)
                for j in range(i * i, n + 1, i):
                    _, box_j, label_j = number_mobs[j]
                    if box_j.get_fill_opacity() == 0:
                        move_pointer_to(box_j)
                        update_text(f"Mark {j} as non-prime (multiple of {i}).")
                        mark_nonprime(box_j, label_j)

        # Cleanup pointer
        self.remove(pointer)
        update_text("All non-primes marked. Highlight remaining primes.")

        # Final primes
        primes = []
        for num, box, label in number_mobs:
            if box.get_fill_opacity() == 0:
                mark_prime(box, label)
                primes.append(str(num))

        # Display primes list
        prime_list = Text("Prime Numbers: " + ", ".join(primes), font_size=28)
        prime_list.next_to(line, DOWN, buff=1)
        self.play(Write(prime_list))

        # Time complexity
        complexity = Text("Time Complexity: O(n log log n)", font_size=28)
        complexity.to_edge(DOWN)
        self.play(Write(complexity))
        self.wait(2)
