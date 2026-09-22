FROM python:3.13-slim

RUN pip install --no-cache-dir "psycopg[binary]"

COPY manager.py /manager.py

CMD ["python", "/manager.py"]
