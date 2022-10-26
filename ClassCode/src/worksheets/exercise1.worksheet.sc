val nums = List(1, 3, 5, 2, 7)
// build List(2, 4, 6, 3, 8)

for(item <- nums) yield item +1



val pairs = List(("A", 2), ("B", 10), ("C", 5))
// build List(("A", 3), ("B", 11), ("C", 6))

for((first,second) <- pairs) yield (first,second +1)