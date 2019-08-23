FROM python:3.7-alpine

# Add Tini.
RUN apk add --update tini

COPY ./web /app
WORKDIR /app

RUN pip install -r requirements.txt

EXPOSE 5000

ENTRYPOINT ["/sbin/tini", "--"]
CMD ["gunicorn", "-w 3", "-b :5000", "app:app"]

