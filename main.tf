terraform {
  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "2.23.1"
    }
  }
}
provider "docker" {
  host = "tcp://localhost:2375"
}
variable "city" {
    type = string
}
variable "composeCommand" {
    type = string
    default = "docker-compose -p cityName up -d"
}
locals {
    command = replace(var.composeCommand, "cityName", var.city)
}

resource "null_resource" "readcontentfile" {
  provisioner "local-exec" {
   command = local.command
   interpreter = ["PowerShell", "-Command"]
  }
}