#!/bin/bash

mvn clean package

docker build -t tfc/tfc-bank .