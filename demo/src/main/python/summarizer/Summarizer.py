import os

import nltk
from nltk.tokenize import sent_tokenize
from nltk.corpus import stopwords

from sumy.parsers.plaintext import PlaintextParser
from sumy.nlp.tokenizers import Tokenizer
from sumy.summarizers.text_rank import TextRankSummarizer
from sumy.utils import get_stop_words

def summarize_text():
    """
    This function reads a text file, preprocesses the text, and summarizes it using the TextRank algorithm.
    The summary is then printed to the console.

    :return: None , prints summary to console for scraper to read
    """
    # Download necessary resources
    nltk.download('punkt')
    nltk.download('stopwords')

    # Input text from file
    path = os.path.join(os.path.dirname(__file__), '..\\..\\java\\app\\outputs\\output.txt')
    with open(path, 'r', encoding='utf-8') as file:
        text = file.read().replace('\n', '')

    # Preprocess text
    stop_words = set(stopwords.words('english'))
    sentences = sent_tokenize(text)
    words = [word for sentence in sentences for word in sentence.split() if word.lower() not in stop_words]

    # Summarize text using TextRank algorithm
    parser = PlaintextParser.from_string(text, Tokenizer('english'))
    summarizer = TextRankSummarizer()
    summarizer.stop_words = get_stop_words('english')
    summary = summarizer(parser.document, sentences_count=3)

    # Combine extracted and compressed sentences into summary
    summary = ' '.join(str(sentence) for sentence in summary)
    print(summary)

# Main
summarize_text()


