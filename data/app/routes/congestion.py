from flask import Blueprint, Response, Flask
import pandas as pd
from datetime import datetime
import json
from flask_cors import CORS
# Blueprints를 정의합니다.
bp = Blueprint('congestion', __name__)
CORS(bp, supports_credentials=True)

congestion_data = pd.read_parquet('congestion_levels.parquet')

@bp.route('/congestion', methods=['GET'])
def get_congestion():
    now = datetime.now()
    current_hour = now.hour 
    current_day = now.weekday()  # 월요일=0, 일요일=6
    
    # 해당 시간과 요일에 맞는 데이터를 필터링
    filtered_data = congestion_data[
        (congestion_data['요일_숫자'] == current_day) & 
        (congestion_data['시간'] == current_hour)
    ]
    
    filtered_data = filtered_data[filtered_data['행정동코드'].notna()][['행정동코드', '혼잡도']]
    
    # 혼잡도를 정수형으로 변환
    filtered_data['혼잡도'] = filtered_data['혼잡도'].astype(int)
    
    # NaN 값을 null로 변환하고 JSON으로 변환
    result = filtered_data.where(pd.notnull(filtered_data), None).to_dict(orient='records')
    json_response = json.dumps(result, ensure_ascii=False)
    
    response = Response(json_response, content_type="application/json; charset=utf-8", status=200)
    return response
