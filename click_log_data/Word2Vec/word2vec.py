import sys
import time
import json
from pyspark import SparkContext
from pyspark.mllib.feature import Word2Vec

training_file = "demo.txt"
# training_file = "test-neg.txt"
synonyms_data_file = "output.txt"
sc = SparkContext(appName="word2vec")
with open(training_file) as file:
    print(file.read())
inp = sc.textFile(training_file).map(lambda line: line.split(" "))#line.encode("utf8", "ignore").split(" "))
word2vec = Word2Vec()
# Recently, Google developed a method called Word2Vec that captures the context of words,
# while at the same time reducing the size of the data. Word2Vec is actually two different
# methods: Continuous Bag of Words (CBOW) and Skip-gram. In the CBOW method, the goal is to
# predict a word given the surrounding words. Skip-gram is the converse: we want to predict
# a window of words given a single word (see Figure 1). Both methods use artificial neural
# networks as their classification algorithm. Initially, each word in the vocabulary is a
# random N-dimensional vector. During training, the algorithm learns the optimal vector for
# each word using the CBOW or Skip-gram method.

#millis = int(round(time.time() * 1000))
#model = word2vec.setMinCount(5).setVectorSize(10).setSeed(2017).fit(inp)
#model = word2vec.setVectorSize(10).setSeed(2017).fit(inp)
model = word2vec.setLearningRate(0.02).setMinCount(1).setVectorSize(10).setSeed(2017).fit(inp)
#model = word2vec.setMinCount(5).setVectorSize(10).setSeed(2017).fit(inp)

vec = model.getVectors()
synonyms_data = open(synonyms_data_file, "w")

print("len of vec", len(vec))
for word in vec.keys():
    synonyms = model.findSynonyms(word, 5)
    entry = {}
    entry["word"] = word
    synon_list = []
    for synonym, cosine_distance in synonyms:
        # print(cosine_distance)
        synon_list.append(synonym)
    entry["synonyms"] = synon_list
    synonyms_data.write(json.dumps(entry))
    synonyms_data.write('\n')

synonyms_data.close()

# #print vec
# test_data = ["dslr", "furniture", "shaver", "toddler", "sport","powershot", "xbox", "led"]
# for w in test_data:
#     synonyms = model.findSynonyms(w, 5)
#     print("synonyms of ",w)
#     for word, cosine_distance in synonyms:
#         print("{}: {}".format(word, cosine_distance))
#
# model.save(sc, "../data/model/word2vec_new2")
sc.stop()