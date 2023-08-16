import os

import nltk
from nltk.tokenize import sent_tokenize
from nltk.corpus import stopwords
from sumy.parsers.plaintext import PlaintextParser
from sumy.nlp.tokenizers import Tokenizer
from sumy.summarizers.text_rank import TextRankSummarizer
from sumy.utils import get_stop_words


# download necessary resources
nltk.download('punkt')
nltk.download('stopwords')

# input text from file
path = os.path.join(os.path.dirname(__file__), '..\\..\\java\\app\\outputs\\output.txt')
with open(path, 'r', encoding='utf-8') as file:
    text = file.read().replace('\n', '')


# preprocess text
stop_words = set(stopwords.words('greek'))
sentences = sent_tokenize(text)
words = [word for sentence in sentences for word in sentence.split() if word.lower() not in stop_words]

# summarize text using TextRank algorithm
parser = PlaintextParser.from_string(text, Tokenizer('greek'))
summarizer = TextRankSummarizer()
summarizer.stop_words = get_stop_words('greek')
summary = summarizer(parser.document, sentences_count=3)

# combine extracted and compressed sentences into summary
summary = ' '.join(str(sentence) for sentence in summary)

print(summary)

