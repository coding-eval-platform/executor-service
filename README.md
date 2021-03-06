
# Executor Service [![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0) [![Build Status](https://img.shields.io/circleci/project/github/coding-eval-platform/executor-service/master.svg)](https://circleci.com/gh/coding-eval-platform/executor-service/tree/master) ![GitHub tag (latest SemVer)](https://img.shields.io/github/tag/coding-eval-platform/executor-service.svg)

Service in charge of running code.

## Features

- Compile (when needed) and run code
- Notify execution results

### Supported programming languages

- Java
- Ruby
- C


## Getting started

The following instructions will set the development environment in your local machine, as well as let you run locally an instance of the system.

**Note: This guide covers only Mac OS X setups.**


### Prerequisites

#### Get source code

Clone the repository or download source code:

```
$ git clone https://github.com/coding-eval-platform/executor-service.git
```
or

```
$ wget https://github.com/coding-eval-platform/executor-service/archive/master.zip
```

#### Set up Runtime

**This project requires Java 11**. The following is a guide to install Java 11, and optional ```jenv``` to manage your Java environments.


1.  **Install Java 11:**

    ```
    $ brew cask install java
    ```


    **Note:** If you already had a previous version of Java installed in your system, this will upgrade it. If you want to have several versions of Java installed in your machine, you can use the cask versions tap:


    1.  **Tap the cask versions repository:**

        ```
        $ brew tap homebrew/cask-versions
        ```
    2.  **Install a previous version of Java:**

        ```
        $ brew cask install java8
        ```

2.  **Install and configure jEnv (Optional):**

    Perform this step if you want to run multiple versions of Java in your machine. For more information, check [jEnv webpage](http://www.jenv.be/). Also, check [this guide](https://medium.com/@danielnenkov/multiple-jdk-versions-on-mac-os-x-with-jenv-5ea5522ddc9b).

    1.  **Download software:**

        ```
        $ brew install jenv
        ```

    2.  **Update your ```bash``` or ```zsh``` profile to use jEnv:**

        ##### Bash
        ```
        $ echo 'export PATH="$HOME/.jenv/bin:$PATH"' >> ~/.bash_profile
        $ echo 'eval "$(jenv init -)"' >> ~/.bash_profile
        ```

        If you want to use jEnv now, don't forget to source again your profile:

        ```
        $ source ~/.bash_profile
        ```

        ##### Zsh
        ```
        $ echo ‘export PATH=”$HOME/.jenv/bin:$PATH”’ >> ~/.zshrc
        $ echo ‘eval “$(jenv init -)”’ >> ~/.zshrc
        ```

        If you want to use jEnv now, don't forget to source again your profile:

        ```
        $ source ~/.zshrc
        ```

    3.  **Locate the JDK installations in your machine. They will likely be in the ```/Library/Java/JavaVirtualMachines/``` directory.**

    4.  **Add a Java version to jEnv:**

        ```
        $ jenv add /Library/Java/JavaVirtualMachines/{{jdk-version}}/Contents/Home
        ```
        Replace the ```{{jdk-version}}``` placeholder with an actual version of Java. For example:

        ```
        $ jenv add /Library/Java/JavaVirtualMachines/openjdk-11.0.2.jdk/Contents/Home
        ```

    5.  **Configure jEnv:**

        To set a global version of Java, use the following command:

        ```
        $ jenv global {{jdk-version}}
        ```
        Replace the ```{{jdk-version}}``` placeholder with an actual version of Java. For example:

        ```
        $ jenv global openjdk-11.0.2.jdk
        ```

        You can check the Java versions being managed by jEnv using the following command:

        ```
        $ jenv versions
        ```

        Similarly, you can set the Java Version with a local or shell scope:

        ##### Local Scope
        If you want to set the Java version for the current working directory:

        ```
        $ jenv local {{jdk-version}}
        ```
        ##### Shell Scope
        If you want to set the Java version for the current session:

        ```
        $ jenv shell {{jdk-version}}
        ```


#### Building tool

The building tool used for the project is Maven.

```
$ brew install maven
```

If you have installed jEnv, you can enable the maven plugin, in order to execute maven using the jEnv managed Java:

```
$ jenv enable-plugin maven
```

Restart your shell session in order to have the plugin running.

Check [this resource](https://github.com/gcuisinier/jenv#plugins) for more information about jEnv plugins.


#### Kafka

The project requires a [Kafka](https://kafka.apache.org/) cluster to start and to process requests. Kafka requires [Zookeeper](https://zookeeper.apache.org/).


##### Create a local cluster

1. Instal Zookeeper

```
$ brew install zookeeper
```

2. Install Kafka.

```
$ brew install kafka
```

That's it.

##### Setup project to use the cluster

Set the following property:

- ```spring.kafka.bootstrap-servers```

You can do this by changing the `<project-root>/evaluations-service-application/src/main/resources/application.yml` file, in the development section, or by defining the properties through the command line (with `-Dkey=value` properties, or with `--key=value` properties) when running the application.

The property must be set with the Kafka brokers address and port. You can set several of them. THe format is the following: ```host:port```. Check the following example:

```
spring.kafka.bootstrap-servers:localhost:9092
```

Note: These properties can be filled with the values of a local cluster, or with the values of a remote cluster.


#### Timeout

This service relies on the `timeout` program of the `coreutils` package. You can install it with the following command:

```bash
$ brew install coreutils
```


### Build

1. Install artifacts:

	```
	$ cd <project-root>
	$ mvn clean install
	```

	Doing this will let you access all modules defined in the project scope.

2. Build the project:

	```
	$ mvn clean pacakge
	```

	**Note:** In case you change the ```<project-root>/executor-service-application/src/main/resources/application.yml```, you must build again the project. Otherwise, if you want to change a property on the fly, use command line properties.


### Run

You can run the application using the following command:

```
$ export EXEC_SERVICE_VERSION=<project-version>
$ java [-Dkey=value properties] -jar <project-root>/executor-service-application/target/executor-service-application-$EXEC_SERVICE_VERSION.jar [--key=value properties]
```

The following is a full example of how to run the application:

```
$ export EXEC_SERVICE_VERSION=<project-version>
java \
	-Dspring.kafka.bootstrap-servers=localhost:9092 \
	-jar <project-root>/executor-service-application/target/executor-service-application-0.0.1-SNAPSHOT.jar \
	--spring.profiles.active=dev
```

## Adding support for new languages

Adding support for a new language is very straightforward. Just follow the following steps:

1. Add a new value in the `Language` enum. This value will represent the new language. Then enum can be found in the models module. Note that this will enable a new property (`code-runner.commands.<new-language>`).

2. Set the `code-runner.commands.<new-language>` property with the command to be run by this service, which will run the said command setting several environment variables that will be explained in the next section. Note that this command must be a valid OS command.

### Runner command

The runner command is the program to be executed to run the code. It is a program to run programs. There must be a runner command for each supported language. This command must be a valid OS command (you can check this by executing the command through a clean shell).

This program will be executed by this service setting the following environment variables:


| Variable              | Description                                                   |
|:----------------------|:--------------------------------------------------------------|
| CODE                  | The code to be run.                                           |
| TIMEOUT               | The timeout given to run the code (just the execution phase). |
| RESULT\_FILE\_NAME    | The file name where results must be stored.                   |


The `RESULT_FILE_NAME` contains the name of the file where the execution result must be stored. The following values are accepted (any other will result in an error):

| Value                 | Description                                               |
|:----------------------|:----------------------------------------------------------|
| COMPLETED             | The command finished successfully.                        |
| TIMEOUT               | The execution timed-out.                                  |
| COMPILE_ERROR         | The code did not compile (only for compiled languages).   |
| INITIALIZATION_ERROR  | There was an error while initializing the command.        |
| UNKNOWN_ERROR         | An unexpected error occured.                              |


Note that this command can be any executable. You can write a bash script, a python program, or even a binary built from a C program, to be called by this service.


### Runner command script template

There is a bash script template that can be used to create new executor programs. You can find it in the `
<project-root>/executor-service-application/executors` directory. The file is called `template.sh`.


### Example: adding python

The following is a tutorial to add Python as a supported language.

1. Add the `PYTHON` value to the `Language` enum and rebuild the project.

2. Copy the `template.sh` file. Store it as `<project-root>/executor-service-application/executors/python.sh`

3. Change the `initialize_code` function in order to store the code in a `main.py`. It should be something like this:

    ```bash
    function initialize_code {
        local CODE=$1
        cat <<< "${CODE}" > ./main.py
    }
    ```

4. Remove the `compile_code` function declaration and call. Python is not compiled

5. Change the `run_code` function in order to run the `main.py` file. It should be something like this:

    ```bash
    function run_code {
        local TIMEOUT=$1
        shift # Shift function arguments as the first one contains the timeout
        timeout ${TIMEOUT} python main.py "$@"
    }
    ```
6. Run the service setting the `code-runner.process-timeout.commands.python` property with the following value: `<project-root>/executor-service-application/executors/python.sh`. For example:

    ```
    java \
        -Dspring.kafka.bootstrap-servers=localhost:9092 \
        -jar <project-root>/executor-service-application/target/executor-service-application-0.0.1-SNAPSHOT.jar \
        --spring.profiles.active=dev
        --code-runner.process-timeout.commands.python=`<project-root>/executor-service-application/executors/python.sh`
    ```
7. Enjoy python as a supported language.

**Note:** This tutorial assumes that the python interpreter is installed.



## Use with Docker

This project includes a ```Dockerfile``` in the ```executor-service-application``` module, together with the [Spotify's dockerfile maven plugin](https://github.com/spotify/dockerfile-maven).


### Build the image

To create an image to run this project in Docker just package the application with maven, and set the ```docker-build``` profile.
You just have to run the following command:

```
$ mvn clean package -P docker-build -Ddocker.image.tag=latest
```

### Run the project

Once you have built the Docker image, just run the following command:

```
$ docker run -p 8000:8000 itbacep/executor-service:latest
```

Note that you have to use the same tag you used to create the image.

Note that you will have to link the container with another container (or the host machine)
in which both a Kafka cluster is running.

### New programming languages

In case support for a new programming language is added, don't forget to set up the `Dockerfile` appropriately. This includes copying the necessary executable, setting the corresponding properties, and installing the necessary software.

For example, continuing with the Python's tutorial, add the following lines to the `Dockerfile`:


```Dockerfile
ENV PYTHON_RUNNER=run-python.sh
COPY ${EXECUTORS_PATH}/python.sh $RUNNERS_PATH/$PYTHON_RUNNER
RUN set -eux; \
        echo "$RUNNERS_PREFIX.python=$PYTHON_RUNNER" >> $CONFIG_LOCATION/$RUNNERS_CONFIG_FILE;
RUN set -eux; \
        apt-get update; \
        apt-get install -y python;
```

**Note:** If you see the `Dockerfile` you will notice that you can put each of the previous lines in the corresponding section of the file.



## CI/CD Workflow

This project is integrated with [CircleCI](https://circleci.com/).

### Pull requests

When a pull request is created, a build will be triggered in CircleCI, which must succeed in order to merge the pull request. This build will just **compile the source code and run tests**.
Note that if still committing to a branch with an open pull request, each push to the said branch will trigger a build.

### Pushes and merges into master
Pushing or merging into ```master``` will also trigger the **compile** and **test** build in CircleCI. If the build succeeds, this will be followed by a Docker phase: it will build a Docker image and push it into DockerHub. This images will be tagged with the commit's hash.

### Releases
A release is performed by tagging in git. Pushing a tag will also trigger the **compile** and **test** build in CircleCI. If the build succeeds, this will be followed by a Docker phase: it will build a Docker image and push it into DockerHub. This images will be tagged with the git's tag.





## License

Copyright 2019 Bellini & Lobo

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


## Authors

* [Juan Marcos Bellini](https://github.com/juanmbellini)
* [Daniel Lobo](https://github.com/lobo)
