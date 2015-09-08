import sys

class Interval:
    def __init__ (self, line):
        splitLine = line.split()
        self.start = int(splitLine[0])
        self.finish = int(splitLine[1]) + self.start
        self.weight = int(splitLine[2])
        self.p = 0
        self.last = 0
        self.value = 0

    def __str__ (self):
        return str(self.start)+" "+str(self.finish)+" "+str(self.weight)

def binarySearch(list1, list2, start): #O(log(n)) [Corresponds to line 40]
    if(len(list2) == 1):
        if(start >= list2[0].finish):
            return (list1.index(list2[0]))
        else:
            return (list1.index(list2[0]) - 1)
    if(list2[len(list2)//2].finish > start):
        return(binarySearch(list1, list2[:len(list2)//2], start))
    else:
        return(binarySearch(list1, list2[len(list2)//2:], start))

intervals = [Interval(line) for line in sys.stdin]
intervals = [intervals[x] for x in range(len(intervals))]
# ================================ Sort ===================================== #1
# This will take log(n) time
sortedIntervals = sorted(intervals, key = lambda interval: interval.finish) # O(log(n))!

for x in sortedIntervals:
    print x
print "---------"

lastSecond = 0
for last in sortedIntervals:
    if last.finish > lastSecond:
        lastSecond = last.finish

# ========================== Getting our P Values =========================== #2
# This will also take log(n) time (Binary Search)

for x in range(len(intervals)):
    if(sortedIntervals[x].start < sortedIntervals[0].finish):
        sortedIntervals[x].p = -1
    else:
        input1 = [y for y in sortedIntervals]
        sortedIntervals[x].p = binarySearch(input1, input1, sortedIntervals[x].start)
        # ^^ This will take at O(log(n))

# ============================ Getting the Array ============================ #3
# This will take linear time

values = [x for x in range(len(sortedIntervals))]
reference = [x for x in range(len(sortedIntervals))]

for j in range(len(intervals)): #This will take at most O(n)
    ourV = 0
    if(sortedIntervals[j].p == -1):
        ourV = 0
    else:
        ourV = values[sortedIntervals[j].p]

    lastVal = values[j-1]
    if(j == 0):
        lastVal = 0
    values[j] = max(sortedIntervals[j].weight + ourV, lastVal)
    if(sortedIntervals[j].weight + ourV > lastVal):
        reference[j] = sortedIntervals[j].p
    else:
        reference[j] = j - 1
    sortedIntervals[j].value = max(sortedIntervals[j].weight + ourV, lastVal)

# ============================= Get our Values ============================== #4
# This will take linear time

processes = []
maxx = max(values)
index = values.index(maxx)
processes.append(sortedIntervals[index])

while(maxx != 0 and reference[index] != -1):    #This will take at most O(n)
    maxx = values[reference[index]]
    index = values.index(maxx)
    processes.append(sortedIntervals[index])

rev = processes[::-1]

'''
 This is where I had to stop! I would then use these
 intervals to go back and select the most severe for each interval of change
'''

# for x in rev:
#     print(x)
