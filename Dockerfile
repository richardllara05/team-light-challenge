FROM gradle:latest
WORKDIR /home/gradle
COPY . .
RUN gradle bootJar
# ENTRYPOINT ["java","-jar","build/libs/team-lightfeather-challenge-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
ENTRYPOINT ["tail"]
CMD ["-f","/dev/null"]