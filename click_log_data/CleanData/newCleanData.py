import sys
import json
import random

import nltk
from nltk.corpus import stopwords
from nltk.tokenize import wordpunct_tokenize
from nltk.stem.porter import PorterStemmer

# nltk.download()
stop_words = set(stopwords.words('english'))
stop_words.update(['.', ',' '"', '\'', '?', '!', ':', ';', '(', ')', '[', ']', '{', '}', '&', '/', '...', '-', '+', '*', '|'])
def cleanData(input):
    #remove stop words
    list_of_token = [token.lower() for token in wordpunct_tokenize(input) if token.lower() not in stop_words ]
    print(list_of_token)
    return list_of_token

if __name__ == '__main__':

    input_file = sys.argv[1]
    output_file = sys.argv[2]
    output = open(output_file, "w")
    url_set = []
    id = 2000
    with open(input_file, "r") as lines:
        for line in lines:
            entry = json.loads(line.strip())
            if "detail_url" in entry and "title" in entry:
                unique_id = hash(entry["detail_url"])
                if unique_id not in url_set:
                    url_set.append(unique_id)
                    tokens = cleanData(entry["query"])
                    entry["query"] = " ".join(tokens)
                    entry["adId"] = id
                    id += 1
                    if entry["price"] == 0.0:
                        entry["price"] = random.randint(30, 50000)
                    keywords = cleanData(entry["title"])
                    entry["keyWords"] = keywords
                    entry["title"] = " ".join(keywords)
                    output.write(json.dumps(entry))
                    output.write("\n")
    output.close()


