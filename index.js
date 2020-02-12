import http from "k6/http";
import { check, sleep } from "k6";

export let options = {
  vus: 10,
  duration: "120s"
};

const timeout = 7500

const assertions = {
  "is status 200": (r) => r.status === 200
}

export default function() {
  check(http.get("http://localhost:8080/users", { timeout }), assertions);
  sleep(1);
};