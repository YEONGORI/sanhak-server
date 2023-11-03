import os
import json
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

# JSON 파일을 읽고 title 필드를 추출하는 함수
def read_json_file(file_path):
    with open(file_path, "r", encoding="utf-8") as file:
        data = json.load(file)
        text_data = [obj.get("title", "") for obj in data]
    return text_data

# 디렉토리 내의 JSON 파일을 읽는 함수
def read_json_files_in_directory(directory):
    text_data = []
    file_titles = []
    for filename in os.listdir(directory):
        if filename.endswith(".json"):
            file_path = os.path.join(directory, filename)
            text_data.extend(read_json_file(file_path))
            file_titles.extend([obj.get("title", "") for obj in json.load(
                open(file_path, "r", encoding="utf-8"))])
    return text_data, file_titles

# 텍스트 데이터를 벡터로 변환하는 함수
def vectorize_text(text_data):
    vectorizer = TfidfVectorizer()
    tfidf_matrix = vectorizer.fit_transform(text_data)
    return tfidf_matrix, vectorizer


# JSON 파일이 있는 디렉토리 경로
directory = "./데이터"
text_data, file_titles = read_json_files_in_directory(directory)
tfidf_matrix, vectorizer = vectorize_text(text_data)

# 비교할 특정 텍스트 선택 (예: 첫 번째 텍스트)
query_text = text_data[9]
query_vector = vectorizer.transform([query_text])

# 유사한 파일 찾기
similarities = cosine_similarity(query_vector, tfidf_matrix).flatten()

# Filter similar documents with similarity greater than or equal to 0.5
filtered_indices = [i for i, sim in enumerate(similarities) if sim >= 0.5]

# Output top 5 similar documents or fewer if not available
top_similar_indices = filtered_indices[:5]
similar_titles = [(file_titles[i], similarities[i])
                  for i in top_similar_indices]
return top_similar_indices
